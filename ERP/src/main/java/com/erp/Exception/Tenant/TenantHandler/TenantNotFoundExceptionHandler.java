package com.erp.Exception.Tenant.TenantHandler;

import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Exception.Tenant.TenantNotFound;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TenantNotFoundExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SimpleErrorResponse> TenantNotFoundHandler(TenantNotFound e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
