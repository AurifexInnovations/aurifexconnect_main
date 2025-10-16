package com.erp.Exception.Admin.Handler;

import com.erp.Exception.Admin.AdminAlreadyExistsException;
import com.erp.Exception.Admin.AdminNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminAlreadyExistsExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SimpleErrorResponse> handlerAdminAlreadyExistsException(AdminAlreadyExistsException ex){
        return ResponseBuilder.error(HttpStatus.CONFLICT,ex.getMessage());
    }

}
