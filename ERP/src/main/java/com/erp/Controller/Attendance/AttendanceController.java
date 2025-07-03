package com.erp.Controller.Attendance;

import com.erp.Dto.Request.AttendanceRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.AttendanceResponse;
import com.erp.Service.Attendance.AttendanceService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    @Operation(
            summary = "User Check-In",
            description = "Endpoint to mark user attendance as check-in for today"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checked in successfully"),
            @ApiResponse(responseCode = "400", description = "user already checked in"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ResponseStructure<AttendanceResponse>> checkIn(
            @Valid @RequestBody Param param) {
        AttendanceResponse response = attendanceService.checkIn(param);
        return ResponseBuilder.success(HttpStatus.OK, "Checked in successfully", response);
    }

    @PostMapping("/check-out")
    @Operation(
            summary = "User Check-Out",
            description = "Endpoint to mark user attendance as check-out for today"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checked out successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or already checked out"),
            @ApiResponse(responseCode = "404", description = "Check-in record not found")
    })
    public ResponseEntity<ResponseStructure<AttendanceResponse>> checkOut(
            @Valid @RequestBody Param param) {
        AttendanceResponse response = attendanceService.checkOut(param);
        return ResponseBuilder.success(HttpStatus.OK, "Checked out successfully", response);
    }

    @PostMapping("/update")
    @Operation(
            summary = "Update Attendance By User ID and Date",
            description = "Update attendance details such as check-in and check-out times"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance updated successfully"),
            @ApiResponse(responseCode = "404", description = "Attendance record not found")
    })
    public ResponseEntity<ResponseStructure<AttendanceResponse>> updateAttendance(
            @Valid @RequestBody AttendanceRequest request) {
        AttendanceResponse response = attendanceService.updateAttendance(request);
        return ResponseBuilder.success(HttpStatus.OK, "Attendance updated successfully", response);
    }

    @PostMapping("/user/records")
    @Operation(
            summary = "Get Attendance Records for User",
            description = "Fetch all attendance records for a specific user by user ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance records for user fetched"),
            @ApiResponse(responseCode = "404", description = "No attendance records found for the user")
    })
    public ResponseEntity<ListResponseStructure<AttendanceResponse>> getByUserId(
            @Valid @RequestBody Param param) {
        List<AttendanceResponse> responses = attendanceService.getByUserId(param);
        return ResponseBuilder.success(HttpStatus.OK, "Attendance records for user fetched", responses);
    }

    @PostMapping("/user/monthly-report")
    @Operation(
            summary = "Get Monthly Attendance Report",
            description = "Fetch monthly attendance report for a user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monthly attendance report fetched"),
            @ApiResponse(responseCode = "404", description = "No attendance records found for the month")
    })
    public ResponseEntity<ListResponseStructure<AttendanceResponse>> getMonthlyReport(
            @Valid @RequestBody AttendanceRequest request) {
        List<AttendanceResponse> responses = attendanceService.getMonthlyReport(request);
        return ResponseBuilder.success(HttpStatus.OK, "Monthly attendance report fetched", responses);
    }

    @PostMapping("/all")
    @Operation(
            summary = "Get All Attendance Records",
            description = "Retrieve all attendance records"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All attendance records fetched"),
            @ApiResponse(responseCode = "404", description = "No attendance records found")
    })
    public ResponseEntity<ListResponseStructure<AttendanceResponse>> getAllAttendances() {
        List<AttendanceResponse> responses = attendanceService.getAllAttendances();
        return ResponseBuilder.success(HttpStatus.OK, "All attendance records fetched", responses);
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete Attendance by User ID and Date",
            description = "Delete attendance record by user ID and date"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Attendance record not found")
    })
    public ResponseEntity<ResponseStructure<String>> deleteAttendanceByUserIDandDate(
            @Valid @RequestBody AttendanceRequest request) {
        attendanceService.deleteAttendanceByUserIDandDate(request);
        return ResponseBuilder.success(HttpStatus.OK, "Attendance deleted successfully", "Deleted successfully");
    }

    @DeleteMapping("/all")
    @Operation(
            summary = "Delete All Attendance Records for User",
            description = "Delete all attendance records for a given user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All attendance records deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "No attendance records found for the user")
    })
    public ResponseEntity<ResponseStructure<AttendanceResponse>> deleteAllAttendances(
            @Valid @RequestBody AttendanceRequest request) {
        AttendanceResponse response = attendanceService.deleteAllAttendances(request);
        return ResponseBuilder.success(HttpStatus.OK, "Attendances Deleted Successfully!", response);
    }
}