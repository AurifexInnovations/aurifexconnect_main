package com.erp.TechnicianApp.TechnicianException.SupportException.Handler;

import com.erp.TechnicianApp.TechnicianException.SupportException.SupportNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerSupportExceptionHandler {

    @ExceptionHandler(SupportNotFoundException.class)
    public ResponseEntity<SimpleErrorResponse> handleSupportNotFound(SupportNotFoundException e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<SimpleErrorResponse> handleUserNotFound(UserNotFoundException e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
