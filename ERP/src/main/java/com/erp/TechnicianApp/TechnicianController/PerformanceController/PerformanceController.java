package com.erp.TechnicianApp.TechnicianController.PerformanceController;

import com.erp.TechnicianApp.TechnicianDto.Request.PerformanceEvaluationRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.PerformanceRecordRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.PerformanceResponse;
import com.erp.TechnicianApp.TechnicianService.PerformanceService.PerformanceService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping("/evaluate")
    public ResponseEntity<ResponseStructure<PerformanceResponse>> evaluatePerformance(
            @Valid @RequestBody PerformanceEvaluationRequest request) {
        PerformanceResponse response = performanceService.evaluatePerformance(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Performance evaluated successfully", response);
    }

    @PostMapping("/record")
    public ResponseEntity<ResponseStructure<PerformanceResponse>> getPerformanceRecord(
            @Valid @RequestBody PerformanceRecordRequest request) {
        PerformanceResponse response = performanceService.getPerformanceRecord(request);
        return ResponseBuilder.success(HttpStatus.OK, "Performance record fetched successfully", response);
    }

    @GetMapping("/history/{technicianId}")
    public ResponseEntity<ListResponseStructure<PerformanceResponse>> getPerformanceHistory(
            @PathVariable Long technicianId) {
        List<PerformanceResponse> responses = performanceService.getPerformanceHistory(technicianId);
        return ResponseBuilder.success(HttpStatus.OK, "Performance history fetched successfully", responses);
    }
}
