package com.erp.Enum;

/**
 * Bank statement line reconciliation status
 */
public enum ReconciliationStatus {
    UNRECONCILED("Unreconciled - Not yet matched with any transaction"),
    MATCHED("Matched - Automatically matched with a transaction"),
    MANUAL("Manual - Manually reconciled by user"),
    DISPUTED("Disputed - Requires investigation"),
    RECONCILED("Reconciled - Successfully reconciled");

    private final String description;

    ReconciliationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
