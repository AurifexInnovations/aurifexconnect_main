package com.erp.TechnicianApp.TechnicianException.Location.Handler;

import com.erp.TechnicianApp.TechnicianException.Location.TechnicianLocationNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TechnicianLocationHandler {

    @ExceptionHandler(TechnicianLocationNotFoundException.class)
    public ResponseEntity<SimpleErrorResponse> handleLocationNotFound(
            TechnicianLocationNotFoundException e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
