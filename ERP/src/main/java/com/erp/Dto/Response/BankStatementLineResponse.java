package com.erp.Dto.Response;

import com.erp.Enum.ReconciliationStatus;
import com.erp.Enum.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankStatementLineResponse {

    private Long lineId;
    private Long statementId;
    private LocalDate transactionDate;
    private LocalDate valueDate;
    private String description;
    private String referenceNumber;
    private String chequeNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private ReconciliationStatus reconciliationStatus;
    private BigDecimal balanceAfter;
    private String notes;
    private LocalDateTime createdDate;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    
    // Reconciliation details
    private Long matchedTransactionEntryId;
    private String matchedVoucherNumber;
    private LocalDateTime reconciliationDate;
    private String reconciledBy;
}
