package com.erp.Controller.Lead;

import com.erp.Dto.Request.LeadRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.LeadMonthlyChartResponse;
import com.erp.Dto.Response.LeadResponse;
import com.erp.Dto.Response.LeadStatusSummaryResponse;
import com.erp.Service.Lead.LeadService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/crm/leads")
@Tag(name = "Lead Controller", description = "Collection of API Endpoints for managing CRM Leads")
public class LeadController {

    private final LeadService leadService;

    @PostMapping("/create")
    @Operation(description = """
            API endpoint to create a new lead in the CRM.
            """,
            responses = {
                    @ApiResponse(responseCode = "201", description = "Lead created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<LeadResponse>> create(@Valid @RequestBody LeadRequest request) {
        LeadResponse response = leadService.create(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Lead created successfully", response);
    }

    @PutMapping("/update")
    @Operation(description = """
            API endpoint to update an existing lead by ID.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lead updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Lead not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<LeadResponse>> update(@RequestParam Long id,
                                                                  @Valid @RequestBody LeadRequest request) {
        LeadResponse response = leadService.update(id, request);
        return ResponseBuilder.success(HttpStatus.OK, "Lead updated successfully", response);
    }

    @PostMapping("/record")
    @Operation(description = """
            API endpoint to fetch a lead by its ID.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lead fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "Lead not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<LeadResponse>> getById(@Valid @RequestBody Param param) {
        LeadResponse response = leadService.getById(param.getId());
        return ResponseBuilder.success(HttpStatus.OK, "Lead fetched successfully", response);
    }

    @GetMapping("/all")
    @Operation(description = """
            API endpoint to fetch all leads from the CRM.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leads fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "No leads found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ListResponseStructure<LeadResponse>> getAll() {
        List<LeadResponse> responses = leadService.getAll();
        return ResponseBuilder.success(HttpStatus.OK, "Leads fetched successfully", responses);
    }

    @DeleteMapping("/delete")
    @Operation(description = """
            API endpoint to delete a lead by its ID.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lead deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Lead not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<String>> delete(@Valid @RequestBody Param param) {
        leadService.delete(param.getId());
        return ResponseBuilder.success(HttpStatus.OK, "Lead deleted successfully", "Deleted successfully");
    }

    @PostMapping("/convert")
    @Operation(description = """
            API endpoint to convert a lead into a contact.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lead converted successfully"),
                    @ApiResponse(responseCode = "404", description = "Lead not found or conversion failed",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<LeadResponse>> convertToContact(@Valid @RequestBody Param param) {
        LeadResponse response = leadService.convertToContact(param.getId());
        return ResponseBuilder.success(HttpStatus.OK, "Lead converted successfully", response);
    }

    @PostMapping("/analytics/monthly")
    @Operation(description = """
            API endpoint to get the number of new leads per day for a given month.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Monthly leads chart data fetched successfully")
            })
    public ResponseEntity<ListResponseStructure<LeadMonthlyChartResponse>> getMonthlyLeadsChart(@Valid @RequestBody Param param) {
        List<LeadMonthlyChartResponse> responses = leadService.getMonthlyLeadsChart(param);
        return ResponseBuilder.success(HttpStatus.OK, "Monthly leads chart data fetched", responses);
    }

    @PostMapping("/analytics/status-summary")
    @Operation(description = """
            API endpoint to get the percentage and count of leads grouped by status.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lead status summary fetched successfully")
            })
    public ResponseEntity<ResponseStructure<LeadStatusSummaryResponse>> getLeadStatusSummary(@Valid @RequestBody Param param) {
        LeadStatusSummaryResponse response = leadService.getLeadStatusSummary(param);
        return ResponseBuilder.success(HttpStatus.OK, "Lead status summary fetched", response);
    }
}