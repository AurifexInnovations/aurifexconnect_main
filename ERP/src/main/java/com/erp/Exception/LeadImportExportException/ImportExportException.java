package com.erp.Exception.LeadImportExportException;

public class ImportExportException extends RuntimeException {
    public ImportExportException(String message) {
        super(message);  // ✅ use RuntimeException's message handling
    }
}
