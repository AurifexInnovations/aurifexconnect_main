package com.erp.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler  {

    private final static String SOMETHING_WENT_WRONG = "Something Went Wrong";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(ResourceNotFoundException ex) {
        log.info("Into [GlobalExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

        log.error("Error [GlobalExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());

        log.info("Exit [GlobalExceptionHandler] [handleGlobalException] ");

        return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(ResourceFoundException ex) {
        log.info("Into [GlobalExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

        log.error("Error [GlobalExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());

        log.info("Exit [GlobalExceptionHandler] [handleGlobalException] ");

        return new ResponseEntity<>(errorResponse , HttpStatus.FOUND);
    }

    @ExceptionHandler(DBReltedException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(DBReltedException ex) {
        log.info("Into [GlobalExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

        log.error("Error [GlobalExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());

        log.info("Exit [GlobalExceptionHandler] [handleGlobalException] ");
        return new ResponseEntity<>(errorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GlobalMessageExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(GlobalMessageExceptionHandler ex) {
        log.info("Into [GlobalMessageExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

        log.error("Error [GlobalMessageExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());

        log.info("Exit [GlobalMessageExceptionHandler] [handleGlobalException] ");
        return new ResponseEntity<>(errorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(RuntimeException ex) {
        log.info("Into [GlobalExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = new ErrorResponse(SOMETHING_WENT_WRONG);

        log.error("Error [GlobalExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());
        log.info("Exit [GlobalExceptionHandler] [handleGlobalException] ");

        return new ResponseEntity<>(errorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.info("Into [GlobalExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = new ErrorResponse(SOMETHING_WENT_WRONG);

        log.error("Error [GlobalExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());

        log.info("Exit [GlobalExceptionHandler] [handleGlobalException] ");

        return new ResponseEntity<>(errorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleRequestNotFound(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
