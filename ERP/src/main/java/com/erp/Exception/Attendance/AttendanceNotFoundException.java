package com.erp.Exception.Attendance;

import lombok.Getter;

@Getter
public class AttendanceNotFoundException extends RuntimeException {
    private String message;
    public AttendanceNotFoundException(String message) {
        this.message = message;
    }
}