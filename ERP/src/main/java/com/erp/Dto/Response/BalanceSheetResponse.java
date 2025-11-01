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
public class BalanceSheetResponse {

    private String reportTitle;
    private LocalDate asOfDate;
    private LocalDate generatedDate;
    private String companyName;
    
    // Assets Section
    private List<BalanceSheetLineResponse> assets;
    private BigDecimal totalAssets;
    
    // Liabilities Section
    private List<BalanceSheetLineResponse> liabilities;
    private BigDecimal totalLiabilities;
    
    // Equity Section
    private List<BalanceSheetLineResponse> equity;
    private BigDecimal totalEquity;
    
    // Validation
    private BigDecimal totalAssetsAndEquity;
    private Boolean isBalanced;
    private BigDecimal difference;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BalanceSheetLineResponse {
        private String accountCode;
        private String accountName;
        private String groupName;
        private String subgroupName;
        private BigDecimal currentAmount;
        private BigDecimal previousAmount;
        private BigDecimal variance;
        private BigDecimal variancePercentage;
    }
}
