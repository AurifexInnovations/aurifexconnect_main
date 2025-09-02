package com.erp.TechnicianApp.TechnicianDto.Request;


import lombok.Data;

@Data
public class PerformanceEvaluationRequest {
    private Long userId;   // ✅ updated
    private Integer month;
    private Integer year;
}
