package com.erp.Enum;

/**
 * Represents the normal balance type for ledger accounts
 */
public enum BalanceType {
    DEBIT("Debit balance - Normal for Assets and Expenses"),
    CREDIT("Credit balance - Normal for Liabilities, Equity, and Income");

    private final String description;

    BalanceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the opposite balance type
     */
    public BalanceType getOpposite() {
        return this == DEBIT ? CREDIT : DEBIT;
    }
}
