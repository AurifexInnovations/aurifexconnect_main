package com.erp.Service.LeaveService;

import com.erp.CustomRepository.LeaveCustomRepository;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.LeaveRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.LeaveResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.LeaveStatus;
import com.erp.Enum.LeaveType;
import com.erp.Exception.Leave.LeaveNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.Leave.LeaveMapper;
import com.erp.Model.Leave;
import com.erp.Model.User;
import com.erp.Repository.Leave.LeaveRepository;
import com.erp.Repository.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final LeaveMapper leaveMapper;
    private final UserRepository userRepository;
    private final int ANNUAL_PAID_LEAVE_ALLOWANCE = 12;

    private final LeaveCustomRepository leaveCustomRepository;
    @Override
    public LeaveResponse createLeaveRequest(LeaveRequest request) {
        validateLeaveDates(request.getStartDate(), request.getEndDate());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));

        Leave leave = leaveMapper.mapToLeave(request);
        leave.setUser(user);
        leave.setStatus(LeaveStatus.PENDING);
        leave.setLeaveType(parseLeaveType(request.getLeaveType()));
        leaveRepository.save(leave);
        return leaveMapper.mapToLeaveResponse(leave);
    }

    @Override
    public LeaveResponse updateLeaveRequestByLeaveId(LeaveRequest request) {
        validateLeaveDates(request.getStartDate(), request.getEndDate());
        Leave leave = leaveRepository.findById(request.getId())
                .orElseThrow(() -> new LeaveNotFoundException("Leave not found with id: " + request.getId()));

        leaveMapper.updateLeave(request, leave);
        leave.setLeaveType(parseLeaveType(request.getLeaveType()));

        leaveRepository.save(leave);
        return leaveMapper.mapToLeaveResponse(leave);
    }

    @Override
    public List<LeaveResponse> getLeaveRequestsByUserId(Param param) {
        List<Leave> leaves = leaveRepository.findByUserId(param.getUserId());
        if (leaves.isEmpty()) {
            throw new LeaveNotFoundException("No leave records found for user id: " + param.getUserId());
        }
        return leaveMapper.mapToLeaveResponseList(leaves);
    }

    @Override
    public List<LeaveResponse> getLeaveRequestsByDateRange(LeaveRequest request) {
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end dates must not be null.");
        }
        List<Leave> leaves = leaveRepository.findByStartDateBetween(start, end);
        if (leaves.isEmpty()) {
            throw new LeaveNotFoundException("No leave records found between " + start + " and " + end);
        }
        return leaveMapper.mapToLeaveResponseList(leaves);
    }

    @Override
    public List<LeaveResponse> getLeaveRequestsByStatus(LeaveRequest request) {
        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Leave status must not be null.");
        }

        List<Leave> leaves = leaveRepository.findByStatus(request.getStatus());
        if (leaves.isEmpty()) {
            throw new LeaveNotFoundException("No leave records found with status: " + request.getStatus());
        }
        return leaveMapper.mapToLeaveResponseList(leaves);
    }

    @Override
    public List<LeaveResponse> getAllLeaveRequests() {
        List<Leave> leaves = leaveRepository.findAll();
        if (leaves.isEmpty()) {
            throw new LeaveNotFoundException("No leave records found.");
        }
        return leaveMapper.mapToLeaveResponseList(leaves);
    }

    @Override
    public LeaveResponse updateLeaveStatus(LeaveRequest request) {
        Leave leave = leaveRepository.findById(request.getId())
                .orElseThrow(() -> new LeaveNotFoundException("Leave not found with id: " + request.getId()));

        LeaveStatus oldStatus = leave.getStatus();
        LeaveStatus newStatus = request.getStatus();

        if (newStatus == null || !isValidStatus(newStatus)) {
            throw new IllegalArgumentException("Invalid leave status: " + newStatus);
        }

        User user = leave.getUser();

        // If changing status to APPROVED for a PAID leave, check balance
        if (leave.getLeaveType() == LeaveType.PAID
                && oldStatus != LeaveStatus.APPROVED
                && newStatus == LeaveStatus.APPROVED) {

            long leaveDays = java.time.temporal.ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
            int leaveBalance = calculateLeaveBalance(user.getId());
            if (leaveBalance < leaveDays) {
                throw new IllegalArgumentException("Insufficient leave balance to approve paid leave.");
            }
            // Optionally update leaveBalance on the leave entity
            leave.setLeaveBalance(leaveBalance - (int) leaveDays);
        }

        // For any other status changes, including REJECTED, no balance check required
        leave.setStatus(newStatus);
        leaveRepository.save(leave);
        return leaveMapper.mapToLeaveResponse(leave);
    }

    @Override
    public LeaveResponse deleteLeaveRequest(Param param) {
        Leave leave = leaveRepository.findById(param.getId())
                .orElseThrow(() -> new LeaveNotFoundException("Leave not found with id: " + param.getId()));

        // No need to update leave balance because it’s dynamic and calculated
        leaveRepository.delete(leave);
        return leaveMapper.mapToLeaveResponse(leave);
    }

    private int calculateLeaveBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        LocalDate joiningDate = user.getCreatedAt();
        LocalDate today = LocalDate.now();

        // Calculate how many months completed between joining date and today
        int monthsWorked = joiningDate.until(today).getYears() * 12 + joiningDate.until(today).getMonths();

        // Limit prorated leave allowance to the yearly max (e.g. 12)
        int proratedLeaveAllowance = Math.min(monthsWorked, ANNUAL_PAID_LEAVE_ALLOWANCE);

        List<Leave> approvedPaidLeaves = leaveRepository.findByUserIdAndStatusAndLeaveType(
                userId, LeaveStatus.APPROVED, LeaveType.PAID);

        int usedDays = 0;
        for (Leave leave : approvedPaidLeaves) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
            usedDays += days;
        }
        return proratedLeaveAllowance - usedDays;
    }

    private void validateLeaveDates(LocalDate start, LocalDate end) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Invalid leave dates: start date must be before or equal to end date.");
        }
    }

    private LeaveType parseLeaveType(String type) {
        if (type == null || type.isBlank()) {
            return LeaveType.UNPAID;
        }
        try {
            return LeaveType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LeaveType.UNPAID;
        }
    }

    private boolean isValidStatus(LeaveStatus status) {
        return status == LeaveStatus.PENDING || status == LeaveStatus.APPROVED || status == LeaveStatus.REJECTED;
    }

    @Override
    public ResultDto<LeaveResponse> getLeaveDetails(FilterRequest filterRequest){
        log.info("Into [LeaveServiceImpl] [getLeaveDetails]");

        log.info("[LeaveServiceImpl] [getLeaveDetails]");

        ResultDto<LeaveResponse> leaveResponses = new ResultDto<>();

        try{
             leaveResponses = leaveCustomRepository.getFilteredLeaves(filterRequest);
        }catch (Exception exception){
            log.error("Error [LeaveServiceImpl] [getLeaveDetails] :: {} :: {} " , exception.getMessage() ,exception);
        }

        log.info("Into [LeaveServiceImpl] [getLeaveDetails]");

        return leaveResponses;
    }
}
