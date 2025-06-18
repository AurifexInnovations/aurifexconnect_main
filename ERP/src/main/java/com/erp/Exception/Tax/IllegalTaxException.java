package com.erp.Exception.Tax;

public class IllegalTaxException extends RuntimeException {
    public IllegalTaxException(String message) {
        super(message);
    }
}
