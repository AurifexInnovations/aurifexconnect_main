package com.erp.Service.LeaveService;

import com.erp.Dto.Request.LeaveRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.LeaveResponse;

import java.util.List;

public interface LeaveService {


    LeaveResponse createLeaveRequest(LeaveRequest request);
    LeaveResponse updateLeaveRequestByLeaveId(LeaveRequest request);
    List<LeaveResponse> getLeaveRequestsByUserId(Param param);
    List<LeaveResponse> getLeaveRequestsByDateRange(LeaveRequest request);
    List<LeaveResponse> getLeaveRequestsByStatus(LeaveRequest request);
    List<LeaveResponse> getAllLeaveRequests();
    LeaveResponse updateLeaveStatus(LeaveRequest request);
    LeaveResponse deleteLeaveRequest(Param param);
}
