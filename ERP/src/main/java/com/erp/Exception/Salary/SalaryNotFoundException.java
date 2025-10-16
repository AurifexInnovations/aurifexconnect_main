package com.erp.Exception.Salary;

import lombok.Getter;

@Getter
public class SalaryNotFoundException extends RuntimeException {
    private String message;
    public SalaryNotFoundException(String message) {
        this.message = message;
    }
}
