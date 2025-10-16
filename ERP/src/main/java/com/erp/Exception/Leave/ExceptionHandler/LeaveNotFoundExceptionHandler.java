package com.erp.Exception.Leave.ExceptionHandler;

import com.erp.Exception.Leave.LeaveNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LeaveNotFoundExceptionHandler {

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> leaveNotFoundExceptionHandler(LeaveNotFoundException e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}