package com.erp.Exception.Attendance.ExeptionHandler;

import com.erp.Exception.Attendance.AttendanceAlreadyExistsException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AttendanceAlreadyExistsExceptionHandler {

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> attendanceAlreadyExistsExceptionHandler(AttendanceAlreadyExistsException e){
        return ResponseBuilder.error(HttpStatus.CONFLICT, e.getMessage());
    }
}