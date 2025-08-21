package com.erp.TechnicianApp.Controller.TechnicianAttendance;

import com.erp.TechnicianApp.Dto.Request.AttendanceParam;
import com.erp.TechnicianApp.Dto.Response.MonthlyAttendanceSummaryResponse;
import com.erp.TechnicianApp.Dto.Response.TechnicianAttendanceResponse;
import com.erp.TechnicianApp.Service.TechnicianAttendance.TechnicianAttendanceService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/attendance")
public class TechnicianAttendanceController {

    private final TechnicianAttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<ResponseStructure<TechnicianAttendanceResponse>> checkIn(@RequestBody AttendanceParam attendanceParam){
        TechnicianAttendanceResponse response = attendanceService.checkIn(
                attendanceParam.getTechId(),
                attendanceParam.getTaskId()
        );
        return ResponseBuilder.success(HttpStatus.OK,"Check-in successful", response);
    }

    @PostMapping("/check-out")
    public ResponseEntity<ResponseStructure<TechnicianAttendanceResponse>> checkOut(@RequestBody AttendanceParam attendanceParam){
        TechnicianAttendanceResponse response = attendanceService.checkOut(
                attendanceParam.getTechId(),
                attendanceParam.getTaskId()
        );
        return ResponseBuilder.success(HttpStatus.OK,"Check-out successful", response);
    }

    @GetMapping("/daily")
    public ResponseEntity<ResponseStructure<TechnicianAttendanceResponse>> getDailyAttendance(
            @RequestParam Long technicianId,
            @RequestParam LocalDate date){
        TechnicianAttendanceResponse response = attendanceService.getDailyAttendance(technicianId, date);
        return ResponseBuilder.success(HttpStatus.OK,"Daily attendance fetched successfully", response);
    }

    @GetMapping("/monthly")
    public ResponseEntity<ResponseStructure<MonthlyAttendanceSummaryResponse>> getMonthlyAttendanceSummary(
            @RequestParam Long technicianId,
            @RequestParam int month,
            @RequestParam int year) {

        MonthlyAttendanceSummaryResponse response = attendanceService.getMonthlyAttendanceSummary(technicianId, month, year);

        return ResponseBuilder.success(HttpStatus.OK, "Monthly attendance summary fetched successfully", response);
    }

    @GetMapping("/monthly/all")
    public ResponseEntity<ListResponseStructure<MonthlyAttendanceSummaryResponse>> getAllTechniciansMonthlyAttendance(
            @RequestParam int month,
            @RequestParam int year) {

        List<MonthlyAttendanceSummaryResponse> response = attendanceService.getAllTechniciansMonthlyAttendance(month, year);

        return ResponseBuilder.success(HttpStatus.OK, "All technicians monthly attendance fetched successfully", response);
    }
}

