package com.erp.TechnicianApp.TechnicianService.PerformanceService;

import com.erp.TechnicianApp.TechnicianDto.Request.PerformanceEvaluationRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.PerformanceRecordRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.PerformanceResponse;

import java.util.List;

public interface PerformanceService {
    PerformanceResponse evaluatePerformance(PerformanceEvaluationRequest request);

    PerformanceResponse getPerformanceRecord(PerformanceRecordRequest request);

    List<PerformanceResponse> getPerformanceHistory(Long technicianId);
}
