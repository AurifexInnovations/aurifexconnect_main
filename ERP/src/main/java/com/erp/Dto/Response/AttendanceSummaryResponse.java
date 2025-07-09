package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSummaryResponse {
    private AttendanceCount today;
    private List<MonthlyAttendance> monthly;
}