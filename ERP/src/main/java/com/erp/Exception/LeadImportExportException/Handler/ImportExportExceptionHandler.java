package com.erp.Exception.LeadImportExportException.Handler;

import com.erp.Exception.LeadImportExportException.ImportExportException;
import com.erp.Exception.LeadImportExportException.ExcelExportException;
import com.erp.Exception.LeadImportExportException.InvalidCSVException;
import com.erp.Exception.LeadImportExportException.PDFExportException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ImportExportExceptionHandler {

    @ExceptionHandler(InvalidCSVException.class)
    public ResponseEntity<SimpleErrorResponse> handleInvalidCSV(InvalidCSVException e) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ExcelExportException.class)
    public ResponseEntity<SimpleErrorResponse> handleExcelExport(ExcelExportException e) {
        return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(PDFExportException.class)
    public ResponseEntity<SimpleErrorResponse> handlePdfExport(PDFExportException e) {
        return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ImportExportException.class)
    public ResponseEntity<SimpleErrorResponse> handleGenericImportExport(ImportExportException e) {
        return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Import/Export error: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<SimpleErrorResponse> handleRuntime(RuntimeException e) {
        return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error: " + e.getMessage());
    }
}