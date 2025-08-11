package com.erp.Exception.BankAccount.ExceptionHandler;

import com.erp.Exception.BankAccount.InactiveBankAccountException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InactiveAccountExceptionHandler {

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> inactiveAccountHandler(InactiveBankAccountException e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
