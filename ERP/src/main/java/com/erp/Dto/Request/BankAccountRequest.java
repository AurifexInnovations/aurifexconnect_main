package com.erp.Dto.Request;

import com.erp.Enum.AccountStatus;
import com.erp.Enum.Banks;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountRequest {

    private String accountNumber; // add pattan for valid account number

    private Banks bankName;

    private Double openingBalance;

    private AccountStatus accountStatus;

    private long findBankAccountId;
}