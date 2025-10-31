package com.erp.Dto.Request;

import com.erp.Enum.EntryType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntryRequest {

    @NotNull(message = "Ledger ID is required")
    private Long ledgerId;

    @NotNull(message = "Entry type is required")
    private EntryType entryType;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String description;

    private String referenceNumber;

    private String chequeNumber;

    private String chequeDate;
}
