package com.erp.Exception.Inventory_Exception.ExceptionHendler;

import com.erp.Exception.Inventory_Exception.InsufficientStockException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InsufficientStockExceptionHandler {
    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> InsufficientStockExceptionHandler(InsufficientStockException e){
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST,e.getMessage());
    }
}
