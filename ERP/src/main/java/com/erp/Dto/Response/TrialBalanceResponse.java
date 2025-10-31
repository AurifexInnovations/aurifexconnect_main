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
public class TrialBalanceResponse {

    private String reportTitle;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate generatedDate;
    private String companyName;
    
    private List<TrialBalanceLineResponse> trialBalanceLines;
    private BigDecimal totalDebits;
    private BigDecimal totalCredits;
    private Boolean isBalanced;
    private BigDecimal difference;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrialBalanceLineResponse {
        private String ledgerCode;
        private String ledgerName;
        private String groupName;
        private String subgroupName;
        private BigDecimal debitAmount;
        private BigDecimal creditAmount;
        private BigDecimal openingBalance;
        private BigDecimal closingBalance;
    }
}
