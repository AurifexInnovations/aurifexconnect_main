package com.erp.TechnicianApp.TechnicianException.PerformanceException.Handler;


import com.erp.TechnicianApp.TechnicianException.PerformanceException.PerformanceNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PerformanceExceptionHandler {

    @ExceptionHandler(PerformanceNotFoundException.class)
    public ResponseEntity<SimpleErrorResponse> handlePerformanceNotFound(PerformanceNotFoundException e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
