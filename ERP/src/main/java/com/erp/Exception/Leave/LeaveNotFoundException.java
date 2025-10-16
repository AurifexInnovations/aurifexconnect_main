package com.erp.Exception.Leave;

import lombok.Getter;

@Getter
public class LeaveNotFoundException extends RuntimeException {
    private String message;
    public LeaveNotFoundException(String message) {
        this.message = message;
    }
}
