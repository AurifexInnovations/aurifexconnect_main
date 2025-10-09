package com.erp.Exception;

import org.springframework.http.HttpStatus;


public class GlobalMessageExceptionHandler extends  RuntimeException{

    private final HttpStatus status;


    public GlobalMessageExceptionHandler(String message, HttpStatus status) {
        super(message);
        this.status = status;    }


}
