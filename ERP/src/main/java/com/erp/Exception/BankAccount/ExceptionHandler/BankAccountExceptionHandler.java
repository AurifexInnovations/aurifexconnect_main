package com.erp.Exception.BankAccount.ExceptionHandler;

import com.erp.Exception.BankAccount.BankAccountNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BankAccountExceptionHandler {

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> bankAccountNotFoundHandler(BankAccountNotFoundException e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}