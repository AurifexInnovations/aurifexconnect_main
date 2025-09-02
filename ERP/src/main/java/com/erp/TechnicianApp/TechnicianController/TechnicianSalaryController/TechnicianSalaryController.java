package com.erp.TechnicianApp.TechnicianController.TechnicianSalaryController;

import com.erp.Dto.Request.Param;
import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.TechnicianApp.TechnicianService.TechnicianSalary.TechnicianSalaryService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/technician/salary")
public class TechnicianSalaryController {

    private final TechnicianSalaryService technicianSalaryService;

    @PostMapping("/generate")
    @Operation(summary = "Generate Salary", description = "Generate salary for a technician for a specific month")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salary generated successfully"),
            @ApiResponse(responseCode = "404", description = "Technician not found")
    })
    public ResponseEntity<ResponseStructure<SalaryResponse>> generateSalary(
            @Valid @RequestBody SalaryRequest request) {
        SalaryResponse response = technicianSalaryService.generateSalaryForMonth(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salary generated successfully", response);
    }

    @PostMapping("/by-month")
    @Operation(summary = "Get Salary by Month", description = "Fetch technician salary for a given month")
    public ResponseEntity<ResponseStructure<SalaryResponse>> getSalaryByMonth(
            @Valid @RequestBody SalaryRequest request) {
        SalaryResponse response = technicianSalaryService.getSalaryByUserAndMonth(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salary fetched successfully", response);
    }

    @PostMapping("/by-technician")
    @Operation(summary = "Get Salaries by Technician", description = "Fetch all salary records of a technician")
    public ResponseEntity<ListResponseStructure<SalaryResponse>> getSalariesByTechnician(
            @Valid @RequestBody Param param) {
        List<SalaryResponse> responses = technicianSalaryService.getSalariesByUserId(param);
        return ResponseBuilder.success(HttpStatus.OK, "Salaries fetched successfully", responses);
    }

    @PostMapping("/month-records")
    @Operation(summary = "Get Salaries by Month", description = "Fetch all technician salaries for a given month")
    public ResponseEntity<ListResponseStructure<SalaryResponse>> getSalariesByMonth(
            @Valid @RequestBody SalaryRequest request) {
        List<SalaryResponse> responses = technicianSalaryService.getSalariesByMonth(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salaries fetched successfully", responses);
    }

    @GetMapping("/all")
    @Operation(summary = "Get All Salaries", description = "Fetch all salary records with pagination")
    public ResponseEntity<ListResponseStructure<SalaryResponse>> getAllSalaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<SalaryResponse> responses = technicianSalaryService.getAllSalaries(page, size);
        return ResponseBuilder.success(HttpStatus.OK, "All salaries fetched successfully", responses);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Salary", description = "Update technician salary details")
    public ResponseEntity<ResponseStructure<SalaryResponse>> updateSalary(
            @Valid @RequestBody SalaryRequest request) {
        SalaryResponse response = technicianSalaryService.updateSalary(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salary updated successfully", response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete Salary", description = "Delete technician salary by user and month")
    public ResponseEntity<ResponseStructure<SalaryResponse>> deleteSalary(
            @Valid @RequestBody SalaryRequest request) {
        SalaryResponse response = technicianSalaryService.deleteSalaryByUserAndMonth(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salary deleted successfully", response);
    }

    @PutMapping("/mark-paid")
    @Operation(summary = "Mark Salary as Paid", description = "Mark technician salary as paid")
    public ResponseEntity<ResponseStructure<SalaryResponse>> markSalaryAsPaid(
            @Valid @RequestBody SalaryRequest request) {
        SalaryResponse response = technicianSalaryService.markSalaryAsPaid(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salary marked as paid", response);
    }

    @GetMapping("/overview")
    @Operation(summary = "Get Salary Overview", description = "Fetch monthly and total salary overview for technicians")
    public ResponseEntity<ResponseStructure<Map<String, Object>>> getSalaryOverview(
            @RequestParam int year,
            @RequestParam YearMonth startMonth,
            @RequestParam YearMonth endMonth) {
        Map<String, Object> response = technicianSalaryService.getSalaryOverview(year, startMonth, endMonth);
        return ResponseBuilder.success(HttpStatus.OK, "Salary overview fetched successfully", response);
    }
}
