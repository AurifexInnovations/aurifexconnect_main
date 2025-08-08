package com.erp.Exception.StockTransfer_Exception.StockTransferExceptionHandler;

import com.erp.Exception.StockTransfer_Exception.StockTransferNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StockTransferExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SimpleErrorResponse> handleStockTransferNotFound(StockTransferNotFoundException e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
