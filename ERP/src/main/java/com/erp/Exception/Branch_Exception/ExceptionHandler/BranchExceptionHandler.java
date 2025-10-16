package com.erp.Exception.Branch_Exception.ExceptionHandler;

import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BranchExceptionHandler{

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> branchNotFoundHandler(BranchNotFoundException e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND,e.getMessage());
    }
}