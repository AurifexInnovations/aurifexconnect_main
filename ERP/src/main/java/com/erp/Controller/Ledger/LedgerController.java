package com.erp.Controller.Ledger;

import com.erp.Dto.Request.LedgerRequest;
import com.erp.Dto.Response.LedgerResponse;
import com.erp.Service.LedgerService.LedgerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coa/ledgers")
@AllArgsConstructor
@Tag(name = "Chart of Accounts - Ledgers", description = "APIs for managing ledgers in the chart of accounts")
public class LedgerController {

    private final LedgerService ledgerService;

    @PostMapping
    @Operation(summary = "Create a new ledger", description = "Creates a new ledger in the chart of accounts")
    public ResponseEntity<LedgerResponse> createLedger(
            @Valid @RequestBody LedgerRequest request) {
        LedgerResponse response = ledgerService.createLedger(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{ledgerId}")
    @Operation(summary = "Update a ledger", description = "Updates an existing ledger")
    public ResponseEntity<LedgerResponse> updateLedger(
            @Parameter(description = "Ledger ID") @PathVariable Long ledgerId,
            @Valid @RequestBody LedgerRequest request) {
        LedgerResponse response = ledgerService.updateLedgerInfo(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{ledgerId}")
    @Operation(summary = "Get ledger by ID", description = "Retrieves ledger details by ID")
    public ResponseEntity<LedgerResponse> getLedgerById(
            @Parameter(description = "Ledger ID") @PathVariable Long ledgerId) {
        // Using existing method with CommanParam
        com.erp.Dto.Request.CommanParam param = new com.erp.Dto.Request.CommanParam();
        param.setId(ledgerId);
        List<LedgerResponse> responses = ledgerService.getLedgerByIdOrName(param);
        if (responses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(responses.get(0));
    }

    @GetMapping
    @Operation(summary = "Get all ledgers", description = "Retrieves all ledgers")
    public ResponseEntity<List<LedgerResponse>> getAllLedgers() {
        List<LedgerResponse> response = ledgerService.getAllLedger();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{ledgerId}")
    @Operation(summary = "Delete ledger", description = "Deletes a ledger")
    public ResponseEntity<LedgerResponse> deleteLedger(
            @Parameter(description = "Ledger ID") @PathVariable Long ledgerId) {
        LedgerRequest request = new LedgerRequest();
        request.setLedgerId(ledgerId);
        LedgerResponse response = ledgerService.deleteByLedgerId(request);
        return ResponseEntity.ok(response);
    }
}