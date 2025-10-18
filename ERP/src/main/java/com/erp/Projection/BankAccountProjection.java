package com.erp.Projection;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BankAccountProjection {
    private Long bankAccountId;
    private String accountNumber;
    private String bankName;
    private double openingBalance;
    private double currentBalance;
    private String accountStatus;
    private String createdBy;
    private LocalDateTime createdAt;

}
