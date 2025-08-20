package com.erp.Exception.TaskActivity.Handler;

import com.erp.Exception.TaskActivity.TaskActivityNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskActivityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SimpleErrorResponse> handleTaskActivityNotFound(TaskActivityNotFoundException e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}