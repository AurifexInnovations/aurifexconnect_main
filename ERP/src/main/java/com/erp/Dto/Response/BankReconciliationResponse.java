package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankReconciliationResponse {

    private Long bankAccountId;
    private String bankAccountName;
    private LocalDate fromDate;
    private LocalDate toDate;
    
    // Statement balances
    private BigDecimal statementOpeningBalance;
    private BigDecimal statementClosingBalance;
    private BigDecimal statementTotalDebits;
    private BigDecimal statementTotalCredits;
    
    // Book balances
    private BigDecimal bookOpeningBalance;
    private BigDecimal bookClosingBalance;
    private BigDecimal bookTotalDebits;
    private BigDecimal bookTotalCredits;
    
    // Reconciliation summary
    private BigDecimal reconciledAmount;
    private BigDecimal unreconciledAmount;
    private Integer totalTransactions;
    private Integer reconciledTransactions;
    private Integer unreconciledTransactions;
    
    // Lists
    private List<BankStatementLineResponse> unreconciledStatementLines;
    private List<UnreconciledTransactionResponse> unreconciledBookTransactions;
    private List<ReconciliationMatchResponse> matchedTransactions;
}
