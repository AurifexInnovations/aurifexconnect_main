package com.erp.Exception.Service_Exception.ExceptionHandler;

import com.erp.Exception.Service_Exception.ServiceNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceExceptionHandler
{
    @ExceptionHandler(ServiceNotFoundException.class)
    ResponseEntity<SimpleErrorResponse> handlerServiceNotFound(ServiceNotFoundException e)
    {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
