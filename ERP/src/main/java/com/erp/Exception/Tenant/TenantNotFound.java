package com.erp.Exception.Tenant;

import lombok.Getter;

@Getter
public class TenantNotFound extends RuntimeException {
    public TenantNotFound(String message) {
        super(message);
    }
}
