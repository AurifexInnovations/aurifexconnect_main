package com.erp.Exception.Salary.ExceptionHandler;

import com.erp.Exception.Salary.SalaryNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SalaryExceptionHandler {

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> salaryExceptionHandler(SalaryNotFoundException e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}