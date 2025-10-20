package com.erp.Enum;

/**
 * Comprehensive voucher types for complete accounting system
 */
public enum VoucherType {
    // Primary Vouchers (Existing)
    SALES("Sales Invoice"),
    PURCHASE("Purchase Bill"),
    RECEIPTS("Receipt Voucher"),
    PAYMENTS("Payment Voucher"),
    
    // New Voucher Types
    CONTRA("Contra Voucher - Cash/Bank transfers"),
    JOURNAL("Journal Voucher - Adjustments and provisions"),
    CREDIT_NOTE("Credit Note - Customer adjustments"),
    DEBIT_NOTE("Debit Note - Vendor adjustments"),
    RECURRING("Recurring Voucher - Auto-generated transactions"),
    
    // Special Vouchers
    OPENING_BALANCE("Opening Balance Voucher"),
    CLOSING_ENTRY("Closing Entry Voucher"),
    DEPRECIATION("Depreciation Entry"),
    PROVISION("Provision Entry"),
    ADJUSTMENT("Adjustment Entry");

    private final String description;

    VoucherType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if voucher type affects cash/bank accounts
     */
    public boolean affectsCashBank() {
        return this == RECEIPTS || this == PAYMENTS || this == CONTRA;
    }

    /**
     * Check if voucher type is for adjustments
     */
    public boolean isAdjustmentType() {
        return this == JOURNAL || this == CREDIT_NOTE || this == DEBIT_NOTE || 
               this == ADJUSTMENT || this == PROVISION || this == DEPRECIATION;
    }

    /**
     * Check if voucher type requires approval
     */
    public boolean requiresApproval() {
        return this == SALES || this == PURCHASE || this == JOURNAL || 
               this == CREDIT_NOTE || this == DEBIT_NOTE;
    }
}
