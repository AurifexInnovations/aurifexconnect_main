package com.erp.Exception.Task.TaskHandler;

import com.erp.Exception.Task.TaskNoFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SimpleErrorResponse> taskNotFoundHandler(TaskNoFoundException e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }


}
