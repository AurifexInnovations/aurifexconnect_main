package com.erp.Exception.Attendance.ExeptionHandler;

import com.erp.Exception.Attendance.AttendanceNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AttendanceExceptionHandler {

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> attendanceNotFoundExceptionHandler(AttendanceNotFoundException e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}