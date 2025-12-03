package com.erp.Exception;
import com.erp.Dto.Response.ErrorResponse;
import com.erp.Dto.Response.ValidationErrorResponse;
import com.erp.Exception.AccountGroup.AccountGroupAlreadyExistsException;
import com.erp.Exception.AccountGroup.AccountGroupNotFoundException;
import com.erp.Exception.AccountSubGroup.AccountSubGroupAlreadyExistsException;
import com.erp.Exception.AccountSubGroup.AccountSubGroupNotFoundException;
import com.erp.Exception.Branch_Exception.BranchLimitExceededException;
import com.erp.Exception.Ledger.LedgerAlreadyExistsException;
import com.erp.Exception.Ledger.LedgerNotFoundException;
import com.erp.Exception.Quotation.QuotationNotFoundException;
import com.erp.Exception.SameEmail.SameEmailFoundException;
import com.erp.Exception.ShipmentException.ShipmentNotFoundException;
import com.erp.Exception.User.AccountManagerLimitExceededException;
import com.erp.Exception.User.TechnicianLimitExceededException;
import com.erp.Utility.SimpleErrorResponse;
import lombok.extern.slf4j.Slf4j;
import com.erp.Exception.Ledger.LedgerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler  {

    private final static String SOMETHING_WENT_WRONG = "Something Went Wrong";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(ResourceNotFoundException ex) {
        log.info("Into [GlobalExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .path("/api/v1")
                .build();

        log.error("Error [GlobalExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());

        log.info("Exit [GlobalExceptionHandler] [handleGlobalException] ");

        return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(ResourceFoundException ex) {
        log.info("Into [GlobalExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FOUND.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .path("/api/v1")
                .build();


        log.error("Error [GlobalExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());

        log.info("Exit [GlobalExceptionHandler] [handleGlobalException] ");

        return new ResponseEntity<>(errorResponse , HttpStatus.FOUND);
    }

    @ExceptionHandler(DBReltedException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(DBReltedException ex) {
        log.info("Into [GlobalExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .path("/api/v1")
                .build();


        log.error("Error [GlobalExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());

        log.info("Exit [GlobalExceptionHandler] [handleGlobalException] ");
        return new ResponseEntity<>(errorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GlobalMessageExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(GlobalMessageExceptionHandler ex) {
        log.info("Into [GlobalMessageExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .path("/api/v1")
                .build();


        log.error("Error [GlobalMessageExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());

        log.info("Exit [GlobalMessageExceptionHandler] [handleGlobalException] ");
        return new ResponseEntity<>(errorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(RuntimeException ex) {
        log.info("Into [GlobalExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Something went Wrong ")
                .message(ex.getMessage())
                .path("/api/v1")
                .build();


        log.error("Error [GlobalExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());
        log.info("Exit [GlobalExceptionHandler] [handleGlobalException] ");

        return new ResponseEntity<>(errorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.info("Into [GlobalExceptionHandler] [handleGlobalException] ");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(SOMETHING_WENT_WRONG)
                .message(ex.getMessage())
                .path("/api/v1")
                .build();


        log.error("Error [GlobalExceptionHandler] [handleGlobalException]  :: {} " , ex.getStackTrace());

        log.info("Exit [GlobalExceptionHandler] [handleGlobalException] ");

        return new ResponseEntity<>(errorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleRequestNotFound(BadRequestException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path("/api/v1")
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

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

    @ExceptionHandler(SameEmailFoundException.class)
    public ResponseEntity<ErrorResponse> handleNullPointer(SameEmailFoundException ex) {
        log.error("Null pointer exception: {}", ex.getMessage(), ex);
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Same Email")
                .message(ex.getMessage())
                .path("/api/v1")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
//        log.error("Unexpected error: {}", ex.getMessage(), ex);
//        ErrorResponse error = ErrorResponse.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .error("Internal Server Error")
//                .message("An unexpected error occurred")
//                .path("/api/v1")
//                .build();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }

    @ExceptionHandler(ShipmentNotFoundException.class)
    ResponseEntity<SimpleErrorResponse> shipmentNotFoundHandler(ShipmentNotFoundException e){

        SimpleErrorResponse simpleErrorResponse = new SimpleErrorResponse();
        simpleErrorResponse.setMessage(e.getMessage());
        simpleErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        simpleErrorResponse.setType("Shipment Not Found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(simpleErrorResponse);
    }

    @ExceptionHandler(QuotationNotFoundException.class)
    ResponseEntity<SimpleErrorResponse> quotationNotFoundHandler(QuotationNotFoundException e){

        SimpleErrorResponse simpleErrorResponse = new SimpleErrorResponse();
        simpleErrorResponse.setMessage(e.getMessage());
        simpleErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        simpleErrorResponse.setType("Quotation Not Found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(simpleErrorResponse);
    }

    @ExceptionHandler(BranchLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleBranchLimitException(BranchLimitExceededException ex) {
        log.error("BranchLimitExceededException exception: {}", ex.getMessage(), ex);
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Branch Limit Exceeded")
                .message(ex.getMessage())
                .path("/branch || /branch/update")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(TechnicianLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleTechnicianLimitException(TechnicianLimitExceededException ex) {
        log.error("TechnicianLimitExceededException exception: {}", ex.getMessage(), ex);
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Technician Limit Exceeded")
                .message(ex.getMessage())
                .path("/api/v1/users")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AccountManagerLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleAccountManagerLimitException(AccountManagerLimitExceededException ex) {
        log.error("TechnicianLimitExceededException exception: {}", ex.getMessage(), ex);
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Account Manager Limit Exceeded")
                .message(ex.getMessage())
                .path("/api/v1/users")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}

