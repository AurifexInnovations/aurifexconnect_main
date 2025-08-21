package com.erp.TechnicianApp.Service.TechnicianAttendance;



import com.erp.TechnicianApp.Dto.Response.MonthlyAttendanceSummaryResponse;
import com.erp.TechnicianApp.Dto.Response.TechnicianAttendanceResponse;

import java.time.LocalDate;
import java.util.List;

public interface TechnicianAttendanceService {

    // Mark technician check-in for a task or workday
    TechnicianAttendanceResponse checkIn(Long technicianId, Long taskId);

    // Mark technician check-out from a task or workday
    TechnicianAttendanceResponse checkOut(Long technicianId, Long taskId);

    // Get a single day's attendance for a technician
    TechnicianAttendanceResponse getDailyAttendance(Long technicianId, LocalDate date);

    // Get attendance summary for all technicians in a month (HR report)
    List<MonthlyAttendanceSummaryResponse> getAllTechniciansMonthlyAttendance(int month, int year);

    // Get monthly summary for a single technician
    MonthlyAttendanceSummaryResponse getMonthlyAttendanceSummary(Long technicianId, int month, int year);
}
