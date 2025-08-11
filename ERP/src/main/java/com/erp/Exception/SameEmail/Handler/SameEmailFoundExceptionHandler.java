package com.erp.Exception.SameEmail.Handler;

import com.erp.Exception.SameEmail.SameEmailFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SameEmailFoundExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SimpleErrorResponse> handlerSameEmailFoundException(SameEmailFoundException e){
        return ResponseBuilder.error(HttpStatus.CONFLICT,e.getMessage());
    }

}
