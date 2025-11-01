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
public class CreditNoteVoucherRequest {

    @NotNull(message = "Voucher date is required")
    private LocalDate voucherDate;

    @NotNull(message = "Customer ledger ID is required")
    private Long customerLedgerId;

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

    private Long originalInvoiceId;

    /**
     * For Credit Note: Reduces customer's outstanding balance
     * Debit: Customer Account, Credit: Sales Account or specific adjustment account
     */
}
