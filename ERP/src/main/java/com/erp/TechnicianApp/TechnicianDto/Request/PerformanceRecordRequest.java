package com.erp.TechnicianApp.TechnicianDto.Request;


import lombok.Data;

@Data
public class PerformanceRecordRequest {
    private Long userId;
    private Integer month;
    private Integer year;
}