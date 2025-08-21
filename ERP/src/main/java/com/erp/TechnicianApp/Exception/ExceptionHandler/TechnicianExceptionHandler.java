package com.erp.TechnicianApp.Exception.ExceptionHandler;

import com.erp.TechnicianApp.Exception.TechnicianNotFoundById;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TechnicianExceptionHandler {

    @ExceptionHandler(TechnicianNotFoundById.class)
    public ResponseEntity<SimpleErrorResponse> handleTechnicianNotFoundByIdException(TechnicianNotFoundById e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
