package com.erp.Exception.Ledger;

public class LedgerNotFoundException extends RuntimeException {
    public LedgerNotFoundException(String message) {
        super(message);
    }
}