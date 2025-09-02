package com.erp.TechnicianApp.TechnicianException.Task;

public class TechnicianTaskNotFoundException extends RuntimeException {
    public TechnicianTaskNotFoundException(String message) {
        super(message);
    }
}