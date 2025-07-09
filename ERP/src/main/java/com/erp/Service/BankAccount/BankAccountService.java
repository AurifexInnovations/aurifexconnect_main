package com.erp.Service.BankAccount;

import com.erp.Dto.Request.BankAccountRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Response.BankAccountResponse;
import com.erp.Dto.Response.BankBalanceResponse;

import java.util.List;

public interface BankAccountService {

    BankAccountResponse createBankAccount(BankAccountRequest bankAccountRequest,long ledgerId);

    List<BankAccountResponse> getAllBankAccounts();

    BankAccountResponse updateBankAccount(BankAccountRequest bankAccountId);

    BankAccountResponse findByBankAccountId(CommanParam bankAccountId);

    BankAccountResponse deleteByBankAccountId(BankAccountRequest bankAccountId);

    BankBalanceResponse getCurrentBankBalance();
}