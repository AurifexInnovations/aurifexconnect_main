package com.erp.Exception.BankAccount;

import lombok.Getter;

@Getter
public class BankAccountNotFoundException extends RuntimeException {
    private String message;
    public BankAccountNotFoundException(String message) {
        this.message = message;
    }
}
