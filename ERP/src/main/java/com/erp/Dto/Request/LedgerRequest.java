package com.erp.Dto.Request;

import com.erp.Enum.BalanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerRequest {

    private Long ledgerId;

    @NotBlank(message = "Ledger name is required")
    @Size(max = 255, message = "Ledger name cannot exceed 255 characters")
    private String name;

    @Pattern(regexp = "^[A-Z0-9]{2,20}$", message = "Ledger code must be 2-20 uppercase alphanumeric characters")
    private String ledgerCode;

    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    @Size(max = 20, message = "Phone cannot exceed 20 characters")
    private String phone;

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @Size(max = 15, message = "GST number cannot exceed 15 characters")
    private String gstNo;

    @Size(max = 10, message = "PAN number cannot exceed 10 characters")
    private String panNo;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private BigDecimal openingBalance = BigDecimal.ZERO;

    @Builder.Default
    private BalanceType balanceType = BalanceType.DEBIT;

    @Builder.Default
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal debitLimit = BigDecimal.ZERO;

    @NotNull(message = "Account group ID is required")
    private Long groupId;

    private Long subgroupId;
}