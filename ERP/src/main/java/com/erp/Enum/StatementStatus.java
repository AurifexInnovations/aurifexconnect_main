package com.erp.Enum;

/**
 * Bank statement processing status
 */
public enum StatementStatus {
    IMPORTED("Imported - Statement has been imported but not processed"),
    PROCESSING("Processing - Statement is being processed for reconciliation"),
    PROCESSED("Processed - Statement has been processed"),
    RECONCILED("Reconciled - Statement has been fully reconciled"),
    ERROR("Error - Statement import/processing failed");

    private final String description;

    StatementStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
