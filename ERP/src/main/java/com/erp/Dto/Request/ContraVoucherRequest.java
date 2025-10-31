package com.erp.Dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContraVoucherRequest {

    @NotNull(message = "Voucher date is required")
    private LocalDate voucherDate;

    @NotBlank(message = "Narration is required")
    @Size(max = 500, message = "Narration cannot exceed 500 characters")
    private String narration;

    @NotEmpty(message = "Transaction entries are required")
    @Valid
    private List<TransactionEntryRequest> transactionEntries;

    private String referenceNumber;

    /**
     * For Contra Voucher: Must have exactly 2 entries - one debit and one credit
     * Both entries must be cash/bank accounts
     */
}
