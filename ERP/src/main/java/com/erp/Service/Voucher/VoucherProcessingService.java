package com.erp.Service.Voucher;

import com.erp.Dto.Request.*;
import com.erp.Enum.VoucherType;
import com.erp.Model.TransactionEntry;
import com.erp.Model.Voucher;

import java.util.List;

/**
 * Service for processing different types of vouchers with double-entry logic
 */
public interface VoucherProcessingService {

    /**
     * Process Contra Voucher (Cash/Bank transfers)
     */
    Voucher processContraVoucher(ContraVoucherRequest request);

    /**
     * Process Journal Voucher (Adjustments, provisions)
     */
    Voucher processJournalVoucher(JournalVoucherRequest request);

    /**
     * Process Credit Note Voucher
     */
    Voucher processCreditNoteVoucher(CreditNoteVoucherRequest request);

    /**
     * Process Debit Note Voucher
     */
    Voucher processDebitNoteVoucher(DebitNoteVoucherRequest request);

    /**
     * Process Recurring Voucher
     */
    Voucher processRecurringVoucher(RecurringVoucherRequest request);

    /**
     * Create double-entry transaction entries for a voucher
     */
    List<TransactionEntry> createTransactionEntries(Voucher voucher, List<TransactionEntryRequest> entries);

    /**
     * Validate double-entry principle
     */
    boolean validateDoubleEntry(List<TransactionEntryRequest> entries);

    /**
     * Post voucher (make it effective)
     */
    Voucher postVoucher(Long voucherId);

    /**
     * Reverse a posted voucher
     */
    Voucher reverseVoucher(Long voucherId, String reason);

    /**
     * Approve a voucher
     */
    Voucher approveVoucher(Long voucherId);

    /**
     * Reject a voucher
     */
    Voucher rejectVoucher(Long voucherId, String reason);

    /**
     * Get voucher with all transaction entries
     */
    Voucher getVoucherWithEntries(Long voucherId);

    /**
     * Generate next voucher number for a voucher type
     */
    String generateNextVoucherNumber(VoucherType voucherType);
}
