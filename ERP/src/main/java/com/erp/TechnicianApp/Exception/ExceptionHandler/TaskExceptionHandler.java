package com.erp.TechnicianApp.Exception.ExceptionHandler;



import com.erp.TechnicianApp.Exception.InvalidStatusException;
import com.erp.TechnicianApp.Exception.InvalidStatusTransitionException;
import com.erp.TechnicianApp.Exception.TaskNotFoundById;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskExceptionHandler {
    @ExceptionHandler(TaskNotFoundById.class)
    public ResponseEntity<SimpleErrorResponse> handleTaskNotFoundByIdException(TaskNotFoundById e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND,e.getMessage());
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<SimpleErrorResponse> handleInvalidStatusException(InvalidStatusException e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<SimpleErrorResponse> handleInvalidStatusTransitionException(InvalidStatusTransitionException e){
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
