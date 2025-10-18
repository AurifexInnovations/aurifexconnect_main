package com.erp.Controller.LeaveController;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Request.LeaveRequest;
import com.erp.Dto.Response.LeaveResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Service.LeaveService.LeaveService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/leave")
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/apply")
    @Operation(summary = "Apply Leave",
            description = "API Endpoint to apply for a leave request",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Leave request submitted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ResponseStructure<LeaveResponse>> applyLeave(@Valid @RequestBody LeaveRequest request) {
        LeaveResponse response = leaveService.createLeaveRequest(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Leave request submitted", response);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Leave Request",
            description = "API Endpoint to update leave request by leave ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave request updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ResponseStructure<LeaveResponse>> updateLeave(@Valid @RequestBody LeaveRequest request) {
        LeaveResponse response = leaveService.updateLeaveRequestByLeaveId(request);
        return ResponseBuilder.success(HttpStatus.OK, "Leave request updated", response);
    }

    @PostMapping("/getbyuserid")
    @Operation(summary = "Get Leave Requests by User ID",
            description = "API Endpoint to find leave requests by user ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found leave requests by user"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ListResponseStructure<LeaveResponse>> getLeaveByUserId(@Valid @RequestBody Param param) {
        List<LeaveResponse> responseList = leaveService.getLeaveRequestsByUserId(param);
        return ResponseBuilder.success(HttpStatus.OK, "Leave requests by user", responseList);
    }

    @PostMapping("/date-range")
    @Operation(summary = "Get Leave Requests by Date Range",
            description = "API Endpoint to find leave requests within a date range",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found leave requests by date range"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ListResponseStructure<LeaveResponse>> getLeavesByDateRange(@Valid @RequestBody LeaveRequest request) {
        List<LeaveResponse> response = leaveService.getLeaveRequestsByDateRange(request);
        return ResponseBuilder.success(HttpStatus.OK, "Leave requests by date range", response);
    }

    @PostMapping("/status")
    @Operation(summary = "Get Leave Requests by Status",
            description = "API Endpoint to find leave requests filtered by status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found leave requests by status"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ListResponseStructure<LeaveResponse>> getLeavesByStatus(@Valid @RequestBody LeaveRequest request) {
        List<LeaveResponse> response = leaveService.getLeaveRequestsByStatus(request);
        return ResponseBuilder.success(HttpStatus.OK, "Leave requests by status", response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get All Leave Requests",
            description = "API Endpoint to retrieve all leave requests",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All leave requests retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ListResponseStructure<LeaveResponse>> getAllLeaveRequests() {
        List<LeaveResponse> response = leaveService.getAllLeaveRequests();
        return ResponseBuilder.success(HttpStatus.OK, "All leave requests retrieved", response);
    }

    @PutMapping("/status")
    @Operation(summary = "Update Leave Status",
            description = "API Endpoint to update the status of a leave request (APPROVED, REJECTED, PENDING)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave status updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "404", description = "Leave request not found")
            })
    public ResponseEntity<ResponseStructure<LeaveResponse>> updateLeaveStatus(@Valid @RequestBody LeaveRequest request) {
        LeaveResponse response = leaveService.updateLeaveStatus(request);
        return ResponseBuilder.success(HttpStatus.OK, "Leave status updated", response);
    }

    @PostMapping("/delete")
    @Operation(summary = "Delete Leave Request",
            description = "API Endpoint to delete leave request by Leave ID & UserID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leave request deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ResponseStructure<LeaveResponse>> deleteLeave(@Valid @RequestBody Param param) {
        LeaveResponse response = leaveService.deleteLeaveRequest(param);
        return ResponseBuilder.success(HttpStatus.OK, "Leave deleted", response);
    }

    @PostMapping("/filter")
    @Operation(summary = "Get Leave Filter Details",
            description = "API Endpoint to find leave requests filtered by status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found leave requests by status"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ResponseStructure<ResultDto<LeaveResponse>>> getLeaveDetails(@RequestBody FilterRequest filterRequest) {
        ResultDto<LeaveResponse> response = leaveService.getLeaveDetails(filterRequest);
        return ResponseBuilder.success(HttpStatus.OK, "Leave requests by status", response);
    }
}