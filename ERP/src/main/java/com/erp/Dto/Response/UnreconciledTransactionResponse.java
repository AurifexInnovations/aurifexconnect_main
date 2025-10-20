package com.erp.Dto.Response;

import com.erp.Enum.EntryType;
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
public class UnreconciledTransactionResponse {

    private Long transactionEntryId;
    private Long voucherId;
    private String voucherNumber;
    private LocalDate transactionDate;
    private String description;
    private String referenceNumber;
    private String chequeNumber;
    private BigDecimal amount;
    private EntryType entryType;
    private String ledgerName;
    private String ledgerCode;
    private Boolean isReconciled;
}
