package com.erp.Controller.AccountDashboardView;

import com.erp.Dto.ChartOfAccountsDTO;
import com.erp.Service.AccountDashBoard.IChartOfAccountsService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chart-of-accounts")
@Tag(name = "Chart Of Accounts Controller", description = "APIs for Chart Of Accounts")
@AllArgsConstructor
public class ChartOfAccountsController {

    @Autowired
    private IChartOfAccountsService chartOfAccountsService;

    @GetMapping("/account-id/{accountId}")
    @Operation(description = "Fetch Chart Of Account by ID",
            responses = {@ApiResponse(responseCode = "200", description = "Chart Of Account retrieved")})
    public ResponseEntity<ResponseStructure<ChartOfAccountsDTO>> fetchById(@PathVariable Integer accountId) {
        ChartOfAccountsDTO response = chartOfAccountsService.getById(accountId);
        return ResponseBuilder.success(HttpStatus.OK, "Chart Of Account retrieved successfully", response);
    }

    @PostMapping("/create-account")
    @Operation(description = "Create Chart Of Account",
            responses = {@ApiResponse(responseCode = "201", description = "Chart Of Account created")})
    public ResponseEntity<ResponseStructure<ChartOfAccountsDTO>> create(@Valid @RequestBody ChartOfAccountsDTO dto) {
        ChartOfAccountsDTO response = chartOfAccountsService.create(dto);
        return ResponseBuilder.success(HttpStatus.CREATED, "Chart Of Account created successfully", response);
    }

    @PutMapping("/update-account")
    @Operation(description = "Update Chart Of Account",
            responses = {@ApiResponse(responseCode = "200", description = "Chart Of Account updated")})
    public ResponseEntity<ResponseStructure<ChartOfAccountsDTO>> update(
                                                                        @Valid @RequestBody ChartOfAccountsDTO dto) {
        ChartOfAccountsDTO response = chartOfAccountsService.update(dto.getCoaId(), dto);
        return ResponseBuilder.success(HttpStatus.OK, "Chart Of Account updated successfully", response);
    }

    @DeleteMapping("/delete-account/{accountId}")
    @Operation(description = "Delete Chart Of Account",
            responses = {@ApiResponse(responseCode = "204", description = "Chart Of Account deleted")})
    public ResponseEntity<ResponseStructure<Void>> delete(@PathVariable Integer accountId) {
        chartOfAccountsService.delete(accountId);

        return (ResponseEntity<ResponseStructure<Void>>) (ResponseEntity<?>) ResponseBuilder.success(
                HttpStatus.NO_CONTENT,
                "Chart Of Account deleted successfully",
                null);
    }
}