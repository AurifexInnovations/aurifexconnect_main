package com.erp.Exception.AccountGroup;

public class AccountGroupAlreadyExistsException extends RuntimeException {
    public AccountGroupAlreadyExistsException(String message) {
        super(message);
    }
}
