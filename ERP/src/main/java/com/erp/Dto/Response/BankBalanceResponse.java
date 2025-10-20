package com.erp.Dto.Response;

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
public class BankBalanceResponse {

    private Long bankAccountId;
    private String bankAccountName;
    private String bankAccountNumber;
    private LocalDate asOfDate;
    private BigDecimal bookBalance;
    private BigDecimal statementBalance;
    private BigDecimal unreconciledAmount;
    private Boolean isReconciled;
    private LocalDate lastReconciliationDate;
}