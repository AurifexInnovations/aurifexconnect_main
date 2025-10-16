package com.erp.Exception.User.Handler;

import com.erp.Exception.User.UserInActiveException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserInActiveExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SimpleErrorResponse> handlerUserInActiveException(UserInActiveException e){
        return ResponseBuilder.error(HttpStatus.FORBIDDEN,e.getMessage());
    }
}
