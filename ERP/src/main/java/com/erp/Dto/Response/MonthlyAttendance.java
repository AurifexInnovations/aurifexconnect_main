package com.erp.Dto.Response;

import lombok.Data;

@Data
public class MonthlyAttendance {
    private String label;
    private int present;
    private int absent;
}