package com.erp.Dto.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSummaryChartResponse {
    private int presentDays;
    private int absentDays;
}
