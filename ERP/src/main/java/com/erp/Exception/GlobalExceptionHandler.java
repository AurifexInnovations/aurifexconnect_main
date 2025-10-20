package com.erp.Exception;

import com.erp.Dto.Response.ErrorResponse;
import com.erp.Dto.Response.ValidationErrorResponse;
import com.erp.Exception.AccountGroup.AccountGroupAlreadyExistsException;
import com.erp.Exception.AccountGroup.AccountGroupNotFoundException;
import com.erp.Exception.AccountSubGroup.AccountSubGroupAlreadyExistsException;
import com.erp.Exception.AccountSubGroup.AccountSubGroupNotFoundException;
import com.erp.Exception.Ledger.LedgerAlreadyExistsException;
import com.erp.Exception.Ledger.LedgerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountGroupNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountGroupNotFound(AccountGroupNotFoundException ex) {
        log.error("Account Group not found: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Account Group Not Found")
                .message(ex.getMessage())
                .path("/api/v1/coa/groups")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AccountGroupAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAccountGroupAlreadyExists(AccountGroupAlreadyExistsException ex) {
        log.error("Account Group already exists: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Account Group Already Exists")
                .message(ex.getMessage())
                .path("/api/v1/coa/groups")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AccountSubGroupNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountSubGroupNotFound(AccountSubGroupNotFoundException ex) {
        log.error("Account SubGroup not found: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Account SubGroup Not Found")
                .message(ex.getMessage())
                .path("/api/v1/coa/subgroups")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AccountSubGroupAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAccountSubGroupAlreadyExists(AccountSubGroupAlreadyExistsException ex) {
        log.error("Account SubGroup already exists: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Account SubGroup Already Exists")
                .message(ex.getMessage())
                .path("/api/v1/coa/subgroups")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(LedgerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLedgerNotFound(LedgerNotFoundException ex) {
        log.error("Ledger not found: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Ledger Not Found")
                .message(ex.getMessage())
                .path("/api/v1/coa/ledgers")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(LedgerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleLedgerAlreadyExists(LedgerAlreadyExistsException ex) {
        log.error("Ledger already exists: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Ledger Already Exists")
                .message(ex.getMessage())
                .path("/api/v1/coa/ledgers")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Input validation failed")
                .validationErrors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path("/api/v1")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointer(NullPointerException ex) {
        log.error("Null pointer exception: {}", ex.getMessage(), ex);
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("A required field is missing")
                .path("/api/v1")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path("/api/v1")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
