package com.erp.Exception.Master.MasterExceptionHandler;

import com.erp.Exception.Master.MasterNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MasterNotFoundExceptionHandler {

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> MasterNotFound(MasterNotFoundException e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND,e.getMessage());
    }
}
