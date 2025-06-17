package com.erp.Dto.Response;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceChartResponse {
    private LocalDate date;
    private String status;
}