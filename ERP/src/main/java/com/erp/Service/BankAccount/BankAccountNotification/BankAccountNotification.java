package com.erp.Service.BankAccount.BankAccountNotification;

import com.erp.Model.BankAccount;

public interface BankAccountNotification {
    void notifyBankAccountCreated(BankAccount bankAccount);

    void notifyBankAccountUpdated(BankAccount bankAccount);

    void notifyBankAccountDeleted(BankAccount bankAccount);
}
