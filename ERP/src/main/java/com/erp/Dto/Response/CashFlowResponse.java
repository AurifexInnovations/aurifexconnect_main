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
public class CashFlowResponse {

    private String reportTitle;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate generatedDate;
    private String companyName;
    
    // Operating Activities
    private List<CashFlowLineResponse> operatingActivities;
    private BigDecimal netOperatingCashFlow;
    
    // Investing Activities
    private List<CashFlowLineResponse> investingActivities;
    private BigDecimal netInvestingCashFlow;
    
    // Financing Activities
    private List<CashFlowLineResponse> financingActivities;
    private BigDecimal netFinancingCashFlow;
    
    // Summary
    private BigDecimal openingCashBalance;
    private BigDecimal closingCashBalance;
    private BigDecimal netCashFlow;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashFlowLineResponse {
        private String description;
        private String accountName;
        private BigDecimal cashInflow;
        private BigDecimal cashOutflow;
        private BigDecimal netCashFlow;
    }
}
