package com.erp.Enum;

/**
 * Transaction type for bank statement lines
 */
public enum TransactionType {
    DEBIT("Debit - Money going out"),
    CREDIT("Credit - Money coming in");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the opposite transaction type
     */
    public TransactionType getOpposite() {
        return this == DEBIT ? CREDIT : DEBIT;
    }
}
