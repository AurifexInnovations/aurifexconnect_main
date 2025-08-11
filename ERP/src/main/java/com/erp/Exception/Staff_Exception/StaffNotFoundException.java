package com.erp.Exception.Staff_Exception;

import lombok.Getter;

@Getter
public class StaffNotFoundException extends RuntimeException {
    public StaffNotFoundException(String message) {
        super(message);
    }
}
