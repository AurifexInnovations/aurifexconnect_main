package com.erp.Repository.TransactionEntry;

import com.erp.Enum.EntryType;
import com.erp.Model.Ledger;
import com.erp.Model.TransactionEntry;
import com.erp.Model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionEntryRepository extends JpaRepository<TransactionEntry, Long> {

    /**
     * Find all entries for a voucher
     */
    List<TransactionEntry> findByVoucherOrderByEntryIdAsc(Voucher voucher);

    /**
     * Find all entries for a ledger
     */
    List<TransactionEntry> findByLedgerOrderByCreatedDateDesc(Ledger ledger);

    /**
     * Find entries by entry type for a voucher
     */
    List<TransactionEntry> findByVoucherAndEntryType(Voucher voucher, EntryType entryType);

    /**
     * Find entries by entry type for a ledger
     */
    List<TransactionEntry> findByLedgerAndEntryType(Ledger ledger, EntryType entryType);

    /**
     * Find unreconciled entries
     */
    List<TransactionEntry> findByIsReconciledFalseOrderByCreatedDateAsc();

    /**
     * Find unreconciled entries for a specific ledger
     */
    List<TransactionEntry> findByLedgerAndIsReconciledFalseOrderByCreatedDateAsc(Ledger ledger);

    /**
     * Get total debit amount for a ledger within date range
     */
    @Query("SELECT COALESCE(SUM(te.amount), 0) FROM TransactionEntry te WHERE te.ledger = :ledger AND te.entryType = 'DEBIT' AND te.voucher.voucherDate BETWEEN :startDate AND :endDate")
    Double getTotalDebitAmountForLedger(@Param("ledger") Ledger ledger, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get total credit amount for a ledger within date range
     */
    @Query("SELECT COALESCE(SUM(te.amount), 0) FROM TransactionEntry te WHERE te.ledger = :ledger AND te.entryType = 'CREDIT' AND te.voucher.voucherDate BETWEEN :startDate AND :endDate")
    Double getTotalCreditAmountForLedger(@Param("ledger") Ledger ledger, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get total debit amount for a ledger (all time)
     */
    @Query("SELECT COALESCE(SUM(te.amount), 0) FROM TransactionEntry te WHERE te.ledger = :ledger AND te.entryType = 'DEBIT'")
    Double getTotalDebitAmountForLedger(@Param("ledger") Ledger ledger);

    /**
     * Get total credit amount for a ledger (all time)
     */
    @Query("SELECT COALESCE(SUM(te.amount), 0) FROM TransactionEntry te WHERE te.ledger = :ledger AND te.entryType = 'CREDIT'")
    Double getTotalCreditAmountForLedger(@Param("ledger") Ledger ledger);

    /**
     * Find entries by date range
     */
    List<TransactionEntry> findByVoucher_VoucherDateBetweenOrderByCreatedDateAsc(LocalDate startDate, LocalDate endDate);

    /**
     * Find entries by voucher type and date range
     */
    @Query("SELECT te FROM TransactionEntry te WHERE te.voucher.voucherType = :voucherType AND te.voucher.voucherDate BETWEEN :startDate AND :endDate ORDER BY te.createdDate ASC")
    List<TransactionEntry> findByVoucherTypeAndDateRange(@Param("voucherType") String voucherType, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
