package com.erp.Service.LeaveService;

import com.erp.Dto.Request.LeaveRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.LeaveResponse;
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
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final LeaveMapper leaveMapper;
    private final UserRepository userRepository;

    @Override
    public LeaveResponse createLeaveRequest(LeaveRequest request) {
        validateLeaveDates(request.getStartDate(), request.getEndDate());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));
        Leave leave = leaveMapper.mapToLeave(request);
        leave.setUser(user);
        leave.setStatus(LeaveStatus.PENDING);
        leave.setLeaveType(request.getLeaveType());
        leaveRepository.save(leave);
        return leaveMapper.mapToLeaveResponse(leave);
    }

    @Override
    public LeaveResponse updateLeaveRequestByLeaveId(LeaveRequest request) {
        validateLeaveDates(request.getStartDate(), request.getEndDate());
        Leave leave = leaveRepository.findById(request.getId())
                .orElseThrow(() -> new LeaveNotFoundException("Leave not found with id: " + request.getId()));
        leaveMapper.updateLeave(request, leave);
        leave.setLeaveType(request.getLeaveType());
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

        LeaveStatus status = request.getStatus();
        if (status == null || !isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid leave status: " + status);
        }

        leave.setStatus(status);
        leaveRepository.save(leave);
        return leaveMapper.mapToLeaveResponse(leave);
    }

    @Override
    public LeaveResponse deleteLeaveRequest(Param param) {
        Leave leave = leaveRepository.findById(param.getId())
                .orElseThrow(() -> new LeaveNotFoundException("Leave not found with id: " + param.getId()));
        leaveRepository.delete(leave);
        return leaveMapper.mapToLeaveResponse(leave);
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
}
