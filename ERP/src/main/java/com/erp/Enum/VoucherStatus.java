package com.erp.Enum;

/**
 * Voucher workflow status for approval process
 */
public enum VoucherStatus {
    DRAFT("Draft - Not yet submitted"),
    PENDING_APPROVAL("Pending Approval - Awaiting approval"),
    APPROVED("Approved - Ready for posting"),
    POSTED("Posted - Transaction COMPLETED"),
    REJECTED("Rejected - Requires modification"),
    CANCELLED("Cancelled - Transaction cancelled");

    private final String description;

    VoucherStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if voucher can be edited
     */
    public boolean canEdit() {
        return this == DRAFT || this == PENDING_APPROVAL || this == REJECTED;
    }

    /**
     * Check if voucher can be posted
     */
    public boolean canPost() {
        return this == APPROVED;
    }

    /**
     * Check if voucher is final
     */
    public boolean isFinal() {
        return this == POSTED || this == CANCELLED;
    }
}
