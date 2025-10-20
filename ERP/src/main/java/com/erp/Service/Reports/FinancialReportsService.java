package com.erp.Service.Reports;

import com.erp.Dto.Response.FinancialReportResponse;
import com.erp.Dto.Response.TrialBalanceResponse;
import com.erp.Dto.Response.ProfitLossResponse;
import com.erp.Dto.Response.BalanceSheetResponse;
import com.erp.Dto.Response.CashFlowResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for generating comprehensive financial reports
 */
public interface FinancialReportsService {

    /**
     * Generate Trial Balance Report
     */
    TrialBalanceResponse generateTrialBalance(LocalDate fromDate, LocalDate toDate);

    /**
     * Generate Profit & Loss Statement
     */
    ProfitLossResponse generateProfitLoss(LocalDate fromDate, LocalDate toDate);

    /**
     * Generate Balance Sheet
     */
    BalanceSheetResponse generateBalanceSheet(LocalDate asOfDate);

    /**
     * Generate Cash Flow Statement
     */
    CashFlowResponse generateCashFlow(LocalDate fromDate, LocalDate toDate);

    /**
     * Generate Bank Reconciliation Report
     */
    FinancialReportResponse generateBankReconciliationReport(Long bankAccountId, LocalDate fromDate, LocalDate toDate);

    /**
     * Generate Tax Reports (GST, VAT, TDS)
     */
    List<FinancialReportResponse> generateTaxReports(LocalDate fromDate, LocalDate toDate, String taxType);

    /**
     * Generate Aging Reports
     */
    FinancialReportResponse generateAgingReport(String reportType, LocalDate asOfDate);

    /**
     * Export report to PDF/Excel
     */
    byte[] exportReport(String reportType, LocalDate fromDate, LocalDate toDate, String format);

    /**
     * Generate comparative reports (YoY, QoQ)
     */
    FinancialReportResponse generateComparativeReport(String reportType, LocalDate currentFromDate, LocalDate currentToDate, 
                                                    LocalDate previousFromDate, LocalDate previousToDate);
}
