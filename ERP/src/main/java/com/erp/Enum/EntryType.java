package com.erp.Enum;

/**
 * Represents debit or credit entry in double-entry bookkeeping
 */
public enum EntryType {
    DEBIT("Debit Entry - Left side of account"),
    CREDIT("Credit Entry - Right side of account");

    private final String description;

    EntryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the opposite entry type
     */
    public EntryType getOpposite() {
        return this == DEBIT ? CREDIT : DEBIT;
    }
}
