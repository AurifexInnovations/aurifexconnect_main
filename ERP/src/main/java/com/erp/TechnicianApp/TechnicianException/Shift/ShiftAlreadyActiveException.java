package com.erp.TechnicianApp.TechnicianException.Shift;

public class ShiftAlreadyActiveException extends RuntimeException {
    public ShiftAlreadyActiveException(String message) {
        super(message);
    }
}
