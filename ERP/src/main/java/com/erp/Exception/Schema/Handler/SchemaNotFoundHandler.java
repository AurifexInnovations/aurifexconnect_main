package com.erp.Exception.Schema.Handler;

import com.erp.Exception.Schema.SchemaNotFound;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SchemaNotFoundHandler {

    @ExceptionHandler
    public ResponseEntity<SimpleErrorResponse> handlerSchemaNotFoundException(SchemaNotFound e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}