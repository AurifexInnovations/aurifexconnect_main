package com.erp.Service.StaffService;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.StaffParam;
import com.erp.Dto.Request.StaffRequest;
import com.erp.Dto.Request.PaginationRequest;
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
}
