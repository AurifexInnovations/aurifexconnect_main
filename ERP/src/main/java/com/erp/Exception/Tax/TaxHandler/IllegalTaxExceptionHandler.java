package com.erp.Exception.Tax.TaxHandler;

import com.erp.Exception.Tax.IllegalTaxException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class IllegalTaxExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SimpleErrorResponse> handlerIllegalException(IllegalTaxException e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
