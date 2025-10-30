package com.erp.Dto.Response;

import com.erp.Enum.ReconciliationStatus;
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
public class ReconciliationMatchResponse {

    private Long statementLineId;
    private String statementDescription;
    private LocalDate statementDate;
    private BigDecimal statementAmount;
    
    private Long transactionEntryId;
    private String voucherNumber;
    private LocalDate transactionDate;
    private BigDecimal transactionAmount;
    private String transactionDescription;
    
    private ReconciliationStatus matchStatus;
    private BigDecimal matchAmount;
    private String matchNotes;
    private LocalDateTime matchedDate;
    private String matchedBy;
}
