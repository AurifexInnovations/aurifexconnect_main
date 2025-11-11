package com.erp.Exception.CompnayDetails.ExceptionHandler;

import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.CompnayDetails.CompanyDetailsFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CompanyExceptionHandler {
    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> companyDetailsFoundHandler(CompanyDetailsFoundException e)
    {
        return ResponseBuilder.error(HttpStatus.FOUND,e.getMessage());
    }
}
