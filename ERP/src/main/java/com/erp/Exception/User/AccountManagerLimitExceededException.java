package com.erp.Exception.User;

public class AccountManagerLimitExceededException extends RuntimeException {
    public AccountManagerLimitExceededException(String message) {
        super(message);
    }
}
