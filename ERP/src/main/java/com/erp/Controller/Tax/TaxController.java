package com.erp.Controller.Tax;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.TaxRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.TaxResponse;
import com.erp.Projection.TaxProjection;
import com.erp.Service.Tax.TaxService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import com.erp.Utility.SimpleErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/")
@Tag(name = "Tax Controller", description = "API Endpoints for Managing Tax Data")
public class TaxController {

    private final TaxService taxService;

    @PostMapping("tax")
    @Operation(description = "Create a New Tax Entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tax Created Successfully"),
            })
    public ResponseEntity<ResponseStructure<TaxResponse>> addTax(@Valid @RequestBody TaxRequest taxRequest) {
        TaxResponse response = taxService.addTax(taxRequest);
        return ResponseBuilder.success(HttpStatus.CREATED, "Tax Created", response);
    }

    @PutMapping("tax/update")
    @Operation(description = "Update an Existing Tax",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tax Updated Successfully"),
                    @ApiResponse(responseCode = "404", description = "Tax Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<TaxResponse>> updateTax(@RequestBody TaxRequest taxRequest) {
        TaxResponse response = taxService.updateTax(taxRequest);
        return ResponseBuilder.success(HttpStatus.OK, "Tax Updated Successfully!", response);
    }

    @DeleteMapping("tax/delete")
    @Operation(description = "Delete a Tax By ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tax Deleted Successfully"),
                    @ApiResponse(responseCode = "404", description = "Tax Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<TaxResponse>> deleteTax(@RequestBody CommanParam param) {
        TaxResponse response = taxService.deleteTax(param);
        return ResponseBuilder.success(HttpStatus.OK, "Tax Deleted Successfully!",response);
    }

    @PostMapping("tax/byid")
    @Operation(description = "Retrieve Tax by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tax Found Successfully"),
                    @ApiResponse(responseCode = "404", description = "Tax Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))

                    })
            })
    public ResponseEntity<ResponseStructure<TaxResponse>> getTaxById(@RequestBody CommanParam param) {
        TaxResponse response = taxService.getTaxById(param);
        return ResponseBuilder.success(HttpStatus.OK, "Tax Found Successfully", response);
    }

    @GetMapping("tax/all")
    @Operation(description = "Retrieve All Taxes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All Taxes Found Successfully"),
                    @ApiResponse(responseCode = "404", description = "No Taxes Available", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<ResultDto<TaxResponse>>> getAllTaxes() {
        ResultDto<TaxResponse> response = taxService.getAllTaxes();
        return ResponseBuilder.success(HttpStatus.OK, "All Taxes Found Successfully!", response);
    }

    @GetMapping("/analytics/tax/total")
    public ResponseEntity<ListResponseStructure<Map<String, Object>>> getTotalTaxAnalytics(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Map<String, Object>> response = taxService.getTotalTaxAnalytics(startDate, endDate);
        return ResponseBuilder.success(HttpStatus.OK, "Total tax chart data fetched", response);
    }

    @GetMapping("/analytics/tax/breakup")
    public ResponseEntity<ResponseStructure<Map<String, Double>>> getTaxBreakupAnalytics(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Map<String, Double> response = taxService.getTaxBreakupAnalytics(startDate, endDate);
        return ResponseBuilder.success(HttpStatus.OK, "Tax breakup chart data fetched", response);
    }

    @PostMapping("/tax/find")
    public ResponseEntity<ListResponseStructure<TaxProjection>> findTaxes(
            @RequestBody FilterRequest filterRequest) {
        List<TaxProjection> projections = taxService.findTaxesByFilter(filterRequest);
        return ResponseBuilder.success(
                HttpStatus.OK,
                "Tax(es) Found Successfully!",
                projections
        );
    }

}