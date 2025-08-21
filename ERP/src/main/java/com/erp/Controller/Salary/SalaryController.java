package com.erp.Controller.Salary;

import com.erp.Dto.Request.Param;
import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.Service.SalaryService.SalaryService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/salary")
public class SalaryController {

    private final SalaryService salaryService;

    @PostMapping("/add")
    @Operation(summary = "Add or Generate Salary Detail",
            description = "API Endpoint to add or generate a salary detail for a user and month",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Salary record created/updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ResponseStructure<SalaryResponse>> addSalaryDetail(@RequestBody @Valid SalaryRequest request) {
        SalaryResponse response = salaryService.generateSalaryForMonth(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Salary detail added successfully", response);
    }

    @PostMapping("/user")
    @Operation(summary = "Find Salary records",
            description = "API Endpoint to Find Salary records",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Salary records fetched"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ListResponseStructure<SalaryResponse>> getSalariesByUser(@RequestBody @Valid Param param) {
        List<SalaryResponse> responseList = salaryService.getSalariesByUserId(param);
        return ResponseBuilder.success(HttpStatus.OK, "Salary records fetched", responseList);
    }

    @PostMapping("/user/salary-month")
    @Operation(summary = "Find Salary records",
            description = "API Endpoint to Find Salary records",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Salary records fetched"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ResponseStructure<SalaryResponse>> getUserSalaryByMonth(@RequestBody @Valid SalaryRequest request) {
        SalaryResponse response = salaryService.getSalaryByUserAndMonth(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salary record fetched", response);
    }

    @PutMapping("/pay")
    @Operation(summary = "Mark Salary As Paid",
            description = "API Endpoint to Update Salary records",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Salary marked as paid successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ResponseStructure<SalaryResponse>> markSalaryAsPaid(@RequestBody @Valid SalaryRequest request) {
        SalaryResponse response = salaryService.markSalaryAsPaid(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salary marked as paid", response);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Salary",
            description = "API Endpoint to Update Salary records",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Salary records Updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ResponseStructure<SalaryResponse>> updateSalary(@RequestBody @Valid SalaryRequest request) {
        SalaryResponse response = salaryService.updateSalary(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salary updated successfully", response);
    }

    @PostMapping
    @Operation(summary = "Find All Salary records",
            description = "API Endpoint to Find All Salaries records",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Salaries records fetched"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ListResponseStructure<SalaryResponse>> getAllSalaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<SalaryResponse> salaries = salaryService.getAllSalaries(page, size);
        return ResponseBuilder.success(HttpStatus.OK, "All salaries fetched", salaries);
    }

    @PostMapping("/month")
    @Operation(summary = "Find Salaries records for month",
            description = "API Endpoint to Find Salaries records by month",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Salaries records fetched by month"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    public ResponseEntity<ListResponseStructure<SalaryResponse>> getSalariesByMonth(@RequestBody @Valid SalaryRequest request) {
        List<SalaryResponse> salaries = salaryService.getSalariesByMonth(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salaries for month fetched", salaries);
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete Salary record By userId & Month",
            description = "API Endpoint to delete a salary record by userId and month",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Salary record deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<ResponseStructure<SalaryResponse>> deleteSalaryByUserAndMonth(@RequestBody SalaryRequest request) {
        SalaryResponse response = salaryService.deleteSalaryByUserAndMonth(request);
        return ResponseBuilder.success(HttpStatus.OK, "Salary record deleted successfully!", response);
    }

    @GetMapping("/overview")
    public ResponseEntity<ResponseStructure<Map<String, Object>>> getSalaryOverview(
            @RequestParam("year") int year,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM") YearMonth startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM") YearMonth endDate) {

        Map<String, Object> overview = salaryService.getSalaryOverview(year, startDate, endDate);
        return ResponseBuilder.success(HttpStatus.OK, "Salary overview fetched successfully", overview);
    }
}