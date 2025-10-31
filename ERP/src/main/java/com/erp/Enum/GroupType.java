package com.erp.Enum;

/**
 * Standard accounting group types following double-entry bookkeeping principles
 */
public enum GroupType {
    ASSETS("Assets - Resources owned by the company"),
    LIABILITIES("Liabilities - Debts and obligations owed by the company"),
    EQUITY("Equity - Owner's interest in the company"),
    INCOME("Income - Revenue generated from business operations"),
    EXPENSES("Expenses - Costs incurred in business operations");

    private final String description;

    GroupType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if the group type increases with debit entries
     */
    public boolean isDebitNormal() {
        return this == ASSETS || this == EXPENSES;
    }

    /**
     * Check if the group type increases with credit entries
     */
    public boolean isCreditNormal() {
        return this == LIABILITIES || this == EQUITY || this == INCOME;
    }
}
