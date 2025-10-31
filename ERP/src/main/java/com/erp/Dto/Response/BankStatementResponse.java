package com.erp.Dto.Response;

import com.erp.Enum.StatementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankStatementResponse {

    private Long statementId;
    private Long bankAccountId;
    private String bankAccountName;
    private String bankAccountNumber;
    private LocalDate statementDate;
    private BigDecimal openingBalance;
    private BigDecimal closingBalance;
    private BigDecimal totalDebits;
    private BigDecimal totalCredits;
    private String statementReference;
    private StatementStatus statementStatus;
    private String importSource;
    private String importFileName;
    private String notes;
    private LocalDateTime createdDate;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    private List<BankStatementLineResponse> statementLines;
    private Integer totalLines;
    private Integer reconciledLines;
    private Integer unreconciledLines;
}
