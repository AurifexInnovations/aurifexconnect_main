package com.erp.Controller.Reports;

import com.erp.Dto.Response.*;
import com.erp.Service.Reports.FinancialReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports/financial")
@AllArgsConstructor
@Tag(name = "Financial Reports", description = "APIs for generating comprehensive financial reports")
public class FinancialReportsController {

   private final FinancialReportsService financialReportsService;

   @GetMapping("/trial-balance")
   @Operation(summary = "Generate Trial Balance", description = "Generates trial balance report for the specified period")
   public ResponseEntity<TrialBalanceResponse> generateTrialBalance(
           @Parameter(description = "From date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
           @Parameter(description = "To date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
       TrialBalanceResponse response = financialReportsService.generateTrialBalance(fromDate, toDate);
       return ResponseEntity.ok(response);
   }

   @GetMapping("/profit-loss")
   @Operation(summary = "Generate Profit & Loss Statement", description = "Generates profit and loss statement for the specified period")
   public ResponseEntity<ProfitLossResponse> generateProfitLoss(
           @Parameter(description = "From date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
           @Parameter(description = "To date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
       ProfitLossResponse response = financialReportsService.generateProfitLoss(fromDate, toDate);
       return ResponseEntity.ok(response);
   }

   @GetMapping("/balance-sheet")
   @Operation(summary = "Generate Balance Sheet", description = "Generates balance sheet as of specific date")
   public ResponseEntity<BalanceSheetResponse> generateBalanceSheet(
           @Parameter(description = "As of date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
       BalanceSheetResponse response = financialReportsService.generateBalanceSheet(asOfDate);
       return ResponseEntity.ok(response);
   }

   @GetMapping("/cash-flow")
   @Operation(summary = "Generate Cash Flow Statement", description = "Generates cash flow statement for the specified period")
   public ResponseEntity<CashFlowResponse> generateCashFlow(
           @Parameter(description = "From date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
           @Parameter(description = "To date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
       CashFlowResponse response = financialReportsService.generateCashFlow(fromDate, toDate);
       return ResponseEntity.ok(response);
   }

   @GetMapping("/bank-reconciliation")
   @Operation(summary = "Generate Bank Reconciliation Report", description = "Generates bank reconciliation report")
   public ResponseEntity<FinancialReportResponse> generateBankReconciliationReport(
           @Parameter(description = "Bank account ID") @RequestParam Long bankAccountId,
           @Parameter(description = "From date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
           @Parameter(description = "To date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
       FinancialReportResponse response = financialReportsService.generateBankReconciliationReport(bankAccountId, fromDate, toDate);
       return ResponseEntity.ok(response);
   }

   @GetMapping("/tax")
   @Operation(summary = "Generate Tax Reports", description = "Generates tax reports (GST, VAT, TDS)")
   public ResponseEntity<List<FinancialReportResponse>> generateTaxReports(
           @Parameter(description = "From date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
           @Parameter(description = "To date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
           @Parameter(description = "Tax type") @RequestParam String taxType) {
       List<FinancialReportResponse> response = financialReportsService.generateTaxReports(fromDate, toDate, taxType);
       return ResponseEntity.ok(response);
   }

   @GetMapping("/aging")
   @Operation(summary = "Generate Aging Report", description = "Generates aging report for receivables/payables")
   public ResponseEntity<FinancialReportResponse> generateAgingReport(
           @Parameter(description = "Report type") @RequestParam String reportType,
           @Parameter(description = "As of date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
       FinancialReportResponse response = financialReportsService.generateAgingReport(reportType, asOfDate);
       return ResponseEntity.ok(response);
   }

   @GetMapping("/comparative")
   @Operation(summary = "Generate Comparative Report", description = "Generates comparative reports (YoY, QoQ)")
   public ResponseEntity<FinancialReportResponse> generateComparativeReport(
           @Parameter(description = "Report type") @RequestParam String reportType,
           @Parameter(description = "Current from date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentFromDate,
           @Parameter(description = "Current to date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentToDate,
           @Parameter(description = "Previous from date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate previousFromDate,
           @Parameter(description = "Previous to date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate previousToDate) {
       FinancialReportResponse response = financialReportsService.generateComparativeReport(
               reportType, currentFromDate, currentToDate, previousFromDate, previousToDate);
       return ResponseEntity.ok(response);
   }

   @GetMapping("/export")
   @Operation(summary = "Export Report", description = "Exports report in specified format (PDF/Excel)")
   public ResponseEntity<byte[]> exportReport(
           @Parameter(description = "Report type") @RequestParam String reportType,
           @Parameter(description = "From date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
           @Parameter(description = "To date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
           @Parameter(description = "Export format") @RequestParam(defaultValue = "PDF") String format) {
       byte[] reportData = financialReportsService.exportReport(reportType, fromDate, toDate, format);
       return ResponseEntity.ok()
               .header("Content-Type", "application/" + format.toLowerCase())
               .header("Content-Disposition", "attachment; filename=" + reportType + "_" + fromDate + "_" + toDate + "." + format.toLowerCase())
               .body(reportData);
   }
}
