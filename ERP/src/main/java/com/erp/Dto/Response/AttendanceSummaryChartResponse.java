package com.erp.Dto.Response;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttendanceSummaryChartResponse {
    private int presentDays;
    private int absentDays;
    private int leaveDays;
    private int holidayDays;
}