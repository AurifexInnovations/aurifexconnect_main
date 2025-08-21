package com.erp.TechnicianApp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAttendanceSummaryResponse {

    private Long technicianId;
    private String technicianName;
    private int totalWorkingDays;
    private int presentDays;
    private int absentDays;
    private int leaveDays;
    private int holidayDays;
    private String month;                // Month name or number (e.g., "August 2025")
}
