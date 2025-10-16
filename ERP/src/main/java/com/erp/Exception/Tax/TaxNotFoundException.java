package com.erp.Exception.Tax;

import lombok.Getter;

@Getter
public class TaxNotFoundException extends RuntimeException {
    public TaxNotFoundException(String message) {
        super(message);
    }
}
