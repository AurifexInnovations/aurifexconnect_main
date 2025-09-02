package com.erp.TechnicianApp.TechnicianException.PerformanceException;



import lombok.Getter;

@Getter
public class PerformanceNotFoundException extends RuntimeException {
    private final String message;

    public PerformanceNotFoundException(String message) {
        this.message = message;
    }
}
