package com.erp.Controller.Voucher;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.VoucherResponse;
import com.erp.Enum.VoucherStatus;
import com.erp.Enum.VoucherType;
import com.erp.Service.Voucher.VoucherProcessingService;
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
@RequestMapping("/api/v1/vouchers")
@AllArgsConstructor
@Tag(name = "Enhanced Voucher Management", description = "APIs for comprehensive voucher processing with double-entry logic")
public class EnhancedVoucherController {

    private final VoucherProcessingService voucherProcessingService;

    @PostMapping("/contra")
    @Operation(summary = "Create Contra Voucher", description = "Creates a contra voucher for cash/bank transfers")
    public ResponseEntity<VoucherResponse> createContraVoucher(@Valid @RequestBody ContraVoucherRequest request) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/journal")
    @Operation(summary = "Create Journal Voucher", description = "Creates a journal voucher for adjustments and provisions")
    public ResponseEntity<VoucherResponse> createJournalVoucher(@Valid @RequestBody JournalVoucherRequest request) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/credit-note")
    @Operation(summary = "Create Credit Note", description = "Creates a credit note voucher for customer adjustments")
    public ResponseEntity<VoucherResponse> createCreditNoteVoucher(@Valid @RequestBody CreditNoteVoucherRequest request) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/debit-note")
    @Operation(summary = "Create Debit Note", description = "Creates a debit note voucher for vendor adjustments")
    public ResponseEntity<VoucherResponse> createDebitNoteVoucher(@Valid @RequestBody DebitNoteVoucherRequest request) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/recurring")
    @Operation(summary = "Create Recurring Voucher", description = "Creates a recurring voucher for automatic generation")
    public ResponseEntity<VoucherResponse> createRecurringVoucher(@Valid @RequestBody RecurringVoucherRequest request) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{voucherId}/post")
    @Operation(summary = "Post Voucher", description = "Posts a voucher to make it effective")
    public ResponseEntity<VoucherResponse> postVoucher(
            @Parameter(description = "Voucher ID") @PathVariable Long voucherId) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{voucherId}/approve")
    @Operation(summary = "Approve Voucher", description = "Approves a pending voucher")
    public ResponseEntity<VoucherResponse> approveVoucher(
            @Parameter(description = "Voucher ID") @PathVariable Long voucherId) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{voucherId}/reject")
    @Operation(summary = "Reject Voucher", description = "Rejects a pending voucher")
    public ResponseEntity<VoucherResponse> rejectVoucher(
            @Parameter(description = "Voucher ID") @PathVariable Long voucherId,
            @RequestBody String reason) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{voucherId}/reverse")
    @Operation(summary = "Reverse Voucher", description = "Creates a reversal voucher for a posted transaction")
    public ResponseEntity<VoucherResponse> reverseVoucher(
            @Parameter(description = "Voucher ID") @PathVariable Long voucherId,
            @RequestBody String reason) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{voucherId}")
    @Operation(summary = "Get Voucher with Entries", description = "Retrieves voucher details with all transaction entries")
    public ResponseEntity<VoucherResponse> getVoucherWithEntries(
            @Parameter(description = "Voucher ID") @PathVariable Long voucherId) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.ok().build();
    }

    @GetMapping("/type/{voucherType}")
    @Operation(summary = "Get Vouchers by Type", description = "Retrieves vouchers filtered by type")
    public ResponseEntity<List<VoucherResponse>> getVouchersByType(
            @Parameter(description = "Voucher Type") @PathVariable VoucherType voucherType) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{voucherStatus}")
    @Operation(summary = "Get Vouchers by Status", description = "Retrieves vouchers filtered by status")
    public ResponseEntity<List<VoucherResponse>> getVouchersByStatus(
            @Parameter(description = "Voucher Status") @PathVariable VoucherStatus voucherStatus) {
        // Implementation will be added after creating VoucherMapper
        return ResponseEntity.ok().build();
    }
}
