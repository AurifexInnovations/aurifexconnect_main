package com.erp.Controller.Deal;

import com.erp.Dto.Request.DealRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.DealResponse;
import com.erp.Service.Deal.DealService;
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
@RequestMapping("/api/crm/deals")
@Tag(name = "Deal Controller", description = "Collection of API Endpoints for managing CRM Deals")
public class DealController {

    private final DealService dealService;

    @PostMapping("/create")
    @Operation(description = """
            API endpoint to create a new Deal record
            """,
            responses = {
                    @ApiResponse(responseCode = "201", description = "Deal created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<DealResponse>> create(@Valid @RequestBody DealRequest request) {
        DealResponse response = dealService.create(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Deal created successfully", response);
    }

    @PutMapping("/update")
    @Operation(description = """
            API endpoint to update an existing Deal record by ID
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deal updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Deal not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<DealResponse>> update(@Valid @RequestBody DealRequest request,
                                                                  @RequestParam Long id) {
        DealResponse response = dealService.update(id, request);
        return ResponseBuilder.success(HttpStatus.OK, "Deal updated successfully", response);
    }

    @PostMapping("/record")
    @Operation(description = """
            API endpoint to fetch a Deal by its ID
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deal fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "Deal not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<DealResponse>> getById(@Valid @RequestBody Param param) {
        DealResponse response = dealService.getById(param.getId());
        return ResponseBuilder.success(HttpStatus.OK, "Deal fetched successfully", response);
    }

    @GetMapping("/all")
    @Operation(description = """
            API endpoint to fetch all Deals
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deals fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "No deals found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ListResponseStructure<DealResponse>> getAll() {
        List<DealResponse> responses = dealService.getAll();
        return ResponseBuilder.success(HttpStatus.OK, "Deals fetched successfully", responses);
    }

    @DeleteMapping("/delete")
    @Operation(description = """
            API endpoint to delete a Deal by its ID
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deal deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Deal not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            })
    public ResponseEntity<ResponseStructure<String>> delete(@Valid @RequestBody Param param) {
        dealService.delete(param.getId());
        return ResponseBuilder.success(HttpStatus.OK, "Deal deleted successfully", "Deleted successfully");
    }
}