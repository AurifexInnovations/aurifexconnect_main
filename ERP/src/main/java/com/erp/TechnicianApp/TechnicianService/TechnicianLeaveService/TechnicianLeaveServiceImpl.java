package com.erp.TechnicianApp.TechnicianService.TechnicianLeaveService;

import com.erp.Dto.Request.LeaveRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.LeaveResponse;
import com.erp.Service.Helper.LeaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianLeaveServiceImpl implements TechnicianLeaveService {

    private final LeaveHelper leaveHelper;

    @Override
    public LeaveResponse applyLeave(LeaveRequest request) {
        return leaveHelper.applyLeave(request);
    }

    @Override
    public LeaveResponse updateLeave(LeaveRequest request) {
        return leaveHelper.updateLeave(request);
    }

    @Override
    public List<LeaveResponse> getLeavesByUser(Param param) {
        return leaveHelper.getLeavesByUser(param);
    }

    @Override
    public List<LeaveResponse> getLeavesByDateRange(LeaveRequest request) {
        return leaveHelper.getLeavesByDateRange(request);
    }

    @Override
    public List<LeaveResponse> getLeavesByStatus(LeaveRequest request) {
        return leaveHelper.getLeavesByStatus(request);
    }

    @Override
    public List<LeaveResponse> getAllLeaves() {
        return leaveHelper.getAllLeaves();
    }

    @Override
    public LeaveResponse updateLeaveStatus(LeaveRequest request) {
        return leaveHelper.updateLeaveStatus(request);
    }

    @Override
    public LeaveResponse deleteLeave(Param param) {
        return leaveHelper.deleteLeave(param);
    }
}
