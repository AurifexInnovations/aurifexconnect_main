package com.erp.Exception.Tax.TaxHandler;

import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaxExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SimpleErrorResponse> taxNotFoundHandler(TaxNotFoundException e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
