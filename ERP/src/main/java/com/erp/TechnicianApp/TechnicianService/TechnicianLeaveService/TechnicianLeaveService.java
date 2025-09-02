package com.erp.TechnicianApp.TechnicianService.TechnicianLeaveService;

import com.erp.Dto.Request.LeaveRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.LeaveResponse;

import java.util.List;

public interface TechnicianLeaveService {

    LeaveResponse applyLeave(LeaveRequest request);

    LeaveResponse updateLeave(LeaveRequest request);

    List<LeaveResponse> getLeavesByUser(Param param);

    List<LeaveResponse> getLeavesByDateRange(LeaveRequest request);

    List<LeaveResponse> getLeavesByStatus(LeaveRequest request);

    List<LeaveResponse> getAllLeaves();

    LeaveResponse updateLeaveStatus(LeaveRequest request);

    LeaveResponse deleteLeave(Param param);
}
