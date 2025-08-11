package com.erp.Exception.Voucher;

import lombok.Getter;

@Getter
public class VoucherNotFound extends RuntimeException {

    private final String message;

    public VoucherNotFound(String message) {
        this.message = message;
    }
}
