package com.erp.Exception.Ledger;

public class LedgerAlreadyExistsException extends RuntimeException {
    public LedgerAlreadyExistsException(String message) {
        super(message);
    }
}
