package com.erp.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebitNoteVoucherRequest {

    @NotNull(message = "Voucher date is required")
    private LocalDate voucherDate;

    @NotNull(message = "Vendor ledger ID is required")
    private Long vendorLedgerId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

    @NotBlank(message = "Narration is required")
    @Size(max = 500, message = "Narration cannot exceed 500 characters")
    private String narration;

    private String referenceNumber;

    private Long originalBillId;

    /**
     * For Debit Note: Increases vendor's outstanding balance
     * Debit: Purchase Account or specific adjustment account, Credit: Vendor Account
     */
}
