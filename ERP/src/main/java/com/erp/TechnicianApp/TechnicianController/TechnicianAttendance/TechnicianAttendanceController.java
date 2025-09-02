package com.erp.TechnicianApp.TechnicianController.TechnicianAttendance;

import com.erp.Dto.Request.AttendanceRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.AttendanceChartResponse;
import com.erp.Dto.Response.AttendanceResponse;
import com.erp.Dto.Response.AttendanceSummaryChartResponse;
import com.erp.TechnicianApp.TechnicianService.TechnicianAttendance.TechnicianAttendanceService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/technician/attendance")
public class TechnicianAttendanceController {

    private final TechnicianAttendanceService technicianAttendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<ResponseStructure<AttendanceResponse>> checkIn(
            @Valid @RequestBody Param param) {
        AttendanceResponse response = technicianAttendanceService.checkIn(param);
        return ResponseBuilder.success(HttpStatus.OK, "Technician checked in successfully", response);
    }

    @PostMapping("/check-out")
    public ResponseEntity<ResponseStructure<AttendanceResponse>> checkOut(
            @Valid @RequestBody Param param) {
        AttendanceResponse response = technicianAttendanceService.checkOut(param);
        return ResponseBuilder.success(HttpStatus.OK, "Technician checked out successfully", response);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseStructure<AttendanceResponse>> updateAttendance(
            @Valid @RequestBody AttendanceRequest request) {
        AttendanceResponse response = technicianAttendanceService.updateAttendance(request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician attendance updated successfully", response);
    }

    @PostMapping("/record")
    public ResponseEntity<ResponseStructure<AttendanceResponse>> getByAttendanceId(
            @Valid @RequestBody Param param) {
        AttendanceResponse response = technicianAttendanceService.getByAttendanceId(param);
        return ResponseBuilder.success(HttpStatus.OK, "Technician attendance record found", response);
    }

    @PostMapping("/records")
    public ResponseEntity<ListResponseStructure<AttendanceResponse>> getByUserId(
            @Valid @RequestBody Param param) {
        List<AttendanceResponse> responses = technicianAttendanceService.getByUserId(param);
        return ResponseBuilder.success(HttpStatus.OK, "Technician attendance records fetched", responses);
    }

    @PostMapping("/monthly-report")
    public ResponseEntity<ListResponseStructure<AttendanceResponse>> getMonthlyReport(
            @Valid @RequestBody AttendanceRequest request) {
        List<AttendanceResponse> responses = technicianAttendanceService.getMonthlyReport(request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician monthly report fetched", responses);
    }

    @PostMapping("/all")
    public ResponseEntity<ListResponseStructure<AttendanceResponse>> getAllAttendances() {
        List<AttendanceResponse> responses = technicianAttendanceService.getAllAttendances();
        return ResponseBuilder.success(HttpStatus.OK, "All technician attendance records fetched", responses);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseStructure<String>> deleteAttendanceByUserIDandDate(
            @Valid @RequestBody AttendanceRequest request) {
        technicianAttendanceService.deleteAttendanceByUserIDandDate(request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician attendance deleted successfully", "Deleted successfully");
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<ResponseStructure<String>> deleteAllAttendances(
            @Valid @RequestBody AttendanceRequest request) {
        technicianAttendanceService.deleteAllAttendances(request);
        return ResponseBuilder.success(HttpStatus.OK, "All technician attendances deleted successfully", "Deleted successfully");
    }

    @PostMapping("/analytics/monthly")
    public ResponseEntity<ListResponseStructure<AttendanceChartResponse>> getMonthlyAttendanceAnalytics(
            @Valid @RequestBody AttendanceRequest request) {
        List<AttendanceChartResponse> responses = technicianAttendanceService.getMonthlyAttendanceAnalytics(request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician monthly chart data fetched", responses);
    }

    @PostMapping("/analytics/summary")
    public ResponseEntity<ResponseStructure<AttendanceSummaryChartResponse>> getAttendanceSummaryAnalytics(
            @Valid @RequestBody AttendanceRequest request) {
        AttendanceSummaryChartResponse response = technicianAttendanceService.getAttendanceSummaryAnalytics(request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician summary data fetched", response);
    }
}