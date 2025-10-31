package com.erp.Service.Banking;

import com.erp.Dto.Request.BankStatementImportRequest;
import com.erp.Dto.Request.BankReconciliationRequest;
import com.erp.Dto.Response.BankBalanceResponse;
import com.erp.Dto.Response.BankReconciliationResponse;
import com.erp.Dto.Response.BankStatementResponse;
import com.erp.Dto.Response.UnreconciledTransactionResponse;
import com.erp.Model.BankStatement;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for banking operations including statement import and reconciliation
 */

public interface BankingService {

    /**
     * Import bank statement from file (CSV/Excel)
     */
    BankStatement importBankStatement(BankStatementImportRequest request);

    /**
     * Import bank statement manually
     */
    BankStatement importBankStatementManually(Long bankAccountId, LocalDate statementDate, 
                                            String openingBalance, String closingBalance);

    /**
     * Get bank statement by ID
     */
    BankStatementResponse getBankStatement(Long statementId);

    /**
     * Get all bank statements for an account
     */
    List<BankStatementResponse> getBankStatementsByAccount(Long bankAccountId);

    /**
     * Auto-reconcile bank statement
     */
    BankReconciliationResponse autoReconcileBankStatement(Long statementId);

    /**
     * Manual reconciliation of statement line
     */
    void manualReconcileStatementLine(Long lineId, Long transactionEntryId, String notes);

    /**
     * Get reconciliation report
     */
    BankReconciliationResponse getReconciliationReport(Long bankAccountId, LocalDate fromDate, LocalDate toDate);

    /**
     * Mark statement as reconciled
     */
    void markStatementAsReconciled(Long statementId);

    /**
     * Get unreconciled transactions
     */
    List<UnreconciledTransactionResponse> getUnreconciledTransactions(Long bankAccountId, LocalDate fromDate, LocalDate toDate);

    /**
     * Process bank reconciliation
     */
    BankReconciliationResponse processReconciliation(BankReconciliationRequest request);

    /**
     * Update bank account balance from reconciled statement
     */
    void updateBankAccountBalance(Long bankAccountId, LocalDate asOfDate);

    /**
     * Get bank balance as of specific date
     */
    BankBalanceResponse getBankBalanceAsOf(Long bankAccountId, LocalDate asOfDate);

    /**
     * Export reconciliation report
     */
    byte[] exportReconciliationReport(Long bankAccountId, LocalDate fromDate, LocalDate toDate, String format);
}
