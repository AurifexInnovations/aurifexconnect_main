package com.erp.Dto.Response;

import com.erp.Enum.BalanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerResponse {

    private Long ledgerId;
    private String name;
    private String ledgerCode;
    private String email;
    private String phone;
    private String address;
    private String gstNo;
    private String panNo;
    private Boolean isActive;
    private BigDecimal openingBalance;
    private BalanceType balanceType;
    private BigDecimal creditLimit;
    private BigDecimal debitLimit;
    private BigDecimal currentBalance;
    private LocalDateTime createdDate;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    
    // CoA information
    private Long groupId;
    private String groupName;
    private String groupCode;
    private Long subgroupId;
    private String subgroupName;
    private String subgroupCode;

    public LedgerResponse(Long ledgerId, String name, String ledgerCode, Boolean isActive) {
        this.ledgerId = ledgerId;
        this.name = name;
        this.ledgerCode = ledgerCode;
        this.isActive = isActive;
    }
}