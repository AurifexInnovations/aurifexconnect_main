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
public class ProfitLossResponse {

    private String reportTitle;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate generatedDate;
    private String companyName;
    
    // Income Section
    private List<ProfitLossLineResponse> incomeItems;
    private BigDecimal totalIncome;
    
    // Expense Section
    private List<ProfitLossLineResponse> expenseItems;
    private BigDecimal totalExpenses;
    
    // Final Calculations
    private BigDecimal grossProfit;
    private BigDecimal netProfit;
    private BigDecimal profitMargin;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfitLossLineResponse {
        private String accountCode;
        private String accountName;
        private String groupName;
        private BigDecimal currentPeriodAmount;
        private BigDecimal previousPeriodAmount;
        private BigDecimal variance;
        private BigDecimal variancePercentage;
    }
}
