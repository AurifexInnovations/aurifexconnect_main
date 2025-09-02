package com.erp.TechnicianApp.TechnicianException.Shift;

public class NoActiveShiftException extends RuntimeException {
    public NoActiveShiftException(String message) {
        super(message);
    }
}
