package com.erp.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartOfAccountsDTO {
    private Integer coaId;
    private String accountNumber;
    private String accountName;
    private String accountType;
    private Integer parentId;
    private Boolean isActive;
}