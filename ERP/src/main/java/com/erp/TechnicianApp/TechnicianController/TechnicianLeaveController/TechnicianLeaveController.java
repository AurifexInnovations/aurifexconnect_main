package com.erp.TechnicianApp.TechnicianController.TechnicianLeaveController;

import com.erp.Dto.Request.LeaveRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.LeaveResponse;
import com.erp.TechnicianApp.TechnicianService.TechnicianLeaveService.TechnicianLeaveService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/technician/leave")
public class TechnicianLeaveController {

    private final TechnicianLeaveService technicianLeaveService;

    @PostMapping("/apply")
    @Operation(summary = "Apply Leave (Technician)",
            description = "Technician applies for a leave request",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Leave request submitted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ResponseStructure<LeaveResponse>> applyLeave(@Valid @RequestBody LeaveRequest request) {
        LeaveResponse response = technicianLeaveService.applyLeave(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Technician leave request submitted", response);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Leave Request (Technician)",
            description = "Technician updates a leave request by leave ID")
    public ResponseEntity<ResponseStructure<LeaveResponse>> updateLeave(@Valid @RequestBody LeaveRequest request) {
        LeaveResponse response = technicianLeaveService.updateLeave(request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician leave request updated", response);
    }

    @PostMapping("/getbyuserid")
    @Operation(summary = "Get Technician Leaves by User ID")
    public ResponseEntity<ListResponseStructure<LeaveResponse>> getLeaveByUserId(@Valid @RequestBody Param param) {
        List<LeaveResponse> responseList = technicianLeaveService.getLeavesByUser(param);
        return ResponseBuilder.success(HttpStatus.OK, "Technician leave requests by user", responseList);
    }

    @PostMapping("/date-range")
    @Operation(summary = "Get Technician Leaves by Date Range")
    public ResponseEntity<ListResponseStructure<LeaveResponse>> getLeavesByDateRange(@Valid @RequestBody LeaveRequest request) {
        List<LeaveResponse> response = technicianLeaveService.getLeavesByDateRange(request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician leave requests by date range", response);
    }

    @PostMapping("/status")
    @Operation(summary = "Get Technician Leaves by Status")
    public ResponseEntity<ListResponseStructure<LeaveResponse>> getLeavesByStatus(@Valid @RequestBody LeaveRequest request) {
        List<LeaveResponse> response = technicianLeaveService.getLeavesByStatus(request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician leave requests by status", response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get All Technician Leave Requests")
    public ResponseEntity<ListResponseStructure<LeaveResponse>> getAllLeaveRequests() {
        List<LeaveResponse> response = technicianLeaveService.getAllLeaves();
        return ResponseBuilder.success(HttpStatus.OK, "All technician leave requests retrieved", response);
    }

    @PutMapping("/status")
    @Operation(summary = "Update Technician Leave Status")
    public ResponseEntity<ResponseStructure<LeaveResponse>> updateLeaveStatus(@Valid @RequestBody LeaveRequest request) {
        LeaveResponse response = technicianLeaveService.updateLeaveStatus(request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician leave status updated", response);
    }

    @PostMapping("/delete")
    @Operation(summary = "Delete Technician Leave Request")
    public ResponseEntity<ResponseStructure<LeaveResponse>> deleteLeave(@Valid @RequestBody Param param) {
        LeaveResponse response = technicianLeaveService.deleteLeave(param);
        return ResponseBuilder.success(HttpStatus.OK, "Technician leave request deleted", response);
    }
}
