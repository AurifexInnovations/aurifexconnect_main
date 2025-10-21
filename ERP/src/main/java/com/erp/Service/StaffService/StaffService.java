package com.erp.Service.StaffService;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.StaffResponse;

import java.util.List;

public interface StaffService {

    // Create a new staff
    StaffResponse createStaff(StaffRequest staffRequest);

    // Update existing staff details
    StaffResponse updateStaff(StaffRequest staffRequest);

    // Delete staff by ID
    StaffResponse deleteStaffById(CommanParam param);

    // Get staff by ID, Name, Designation or Status
    List<StaffResponse> getStaffByIdOrNameOrDesignationOrStatus(StaffParam param);

    // Get all staff with pagination
    List<StaffResponse> getAllStaff(PaginationRequest request);

    // Get staff by Branch Id
    List<StaffResponse> getStaffByBranchId(CommanParam param);

    ResultDto<StaffResponse> getStaffDetails(FilterRequest filterRequest);
}
