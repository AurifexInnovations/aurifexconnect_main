package com.erp.Exception.Attendance;

public class AttendanceAlreadyExistsException extends RuntimeException {
    public AttendanceAlreadyExistsException(String message) {
        super(message);
    }
}
