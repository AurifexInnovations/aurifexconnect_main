package com.erp.Controller.Tax;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.TaxAnalyticsRequest;
import com.erp.Dto.Request.TaxRequest;
import com.erp.Dto.Response.TaxResponse;
import com.erp.Service.Tax.TaxService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import com.erp.Utility.SimpleErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<ListResponseStructure<TaxResponse>> getAllTaxes() {
        List<TaxResponse> response = taxService.getAllTaxes();
        return ResponseBuilder.success(HttpStatus.OK, "All Taxes Found Successfully!", response);
    }

    @PostMapping("/analytics/tax/total") //Line or Bar Chart
    @Operation(summary = "Total Tax Collected", description = "Get total tax collected by date range for charting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total tax chart data fetched")
    })
    public ResponseEntity<ListResponseStructure<Map<String, Object>>> getTotalTaxAnalytics(
            @Valid @RequestBody TaxAnalyticsRequest request) {
        List<Map<String, Object>> response = taxService.getTotalTaxAnalytics(request);
        return ResponseBuilder.success(HttpStatus.OK, "Total tax chart data fetched", response);
    }

    @PostMapping("/analytics/tax/breakup") //Pie Chart
    @Operation(summary = "Tax Breakup Chart", description = "Get tax breakup by type (GST, CGST, SGST etc.)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tax breakup chart data fetched")
    })
    public ResponseEntity<ResponseStructure<Map<String, Double>>> getTaxBreakupAnalytics(
            @Valid @RequestBody TaxAnalyticsRequest request) {
        Map<String, Double> response = taxService.getTaxBreakupAnalytics(request);
        return ResponseBuilder.success(HttpStatus.OK, "Tax breakup chart data fetched", response);
    }
}