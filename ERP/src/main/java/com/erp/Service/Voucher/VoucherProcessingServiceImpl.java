package com.erp.Service.Voucher;

import com.erp.Dto.Request.*;
import com.erp.Enum.EntryType;
import com.erp.Enum.VoucherStatus;
import com.erp.Enum.VoucherType;
import com.erp.Exception.Ledger.LedgerNotFoundException;
import com.erp.Exception.Voucher.VoucherNotFound;
import com.erp.Model.Ledger;
import com.erp.Model.TransactionEntry;
import com.erp.Model.Voucher;
import com.erp.Repository.Ledger.LedgerRepository;
import com.erp.Repository.TransactionEntry.TransactionEntryRepository;
import com.erp.Repository.Voucher.VoucherRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class VoucherProcessingServiceImpl implements VoucherProcessingService {

    private final VoucherRepository voucherRepository;
    private final TransactionEntryRepository transactionEntryRepository;
    private final LedgerRepository ledgerRepository;
    private final VoucherService voucherService;

    @Override
    public Voucher processContraVoucher(ContraVoucherRequest request) {
        log.info("Processing Contra Voucher");

        // Validate contra voucher requirements
        validateContraVoucher(request);

        Voucher voucher = new Voucher(VoucherType.CONTRA, request.getVoucherDate(), request.getNarration());
        voucher.setReferenceNumber(request.getReferenceNumber());
        voucher.setVoucherIndex(generateNextVoucherNumber(VoucherType.CONTRA));

        voucher = voucherRepository.save(voucher);

        // Create transaction entries
        List<TransactionEntry> entries = createTransactionEntries(voucher, request.getTransactionEntries());
        voucher.setTransactionEntries(entries);

        // Calculate totals
        calculateVoucherTotals(voucher);

        voucher = voucherRepository.save(voucher);

        log.info("Successfully processed Contra Voucher with ID: {}", voucher.getVoucherId());
        return voucher;
    }

    @Override
    public Voucher processJournalVoucher(JournalVoucherRequest request) {
        log.info("Processing Journal Voucher");

        Voucher voucher = new Voucher(VoucherType.JOURNAL, request.getVoucherDate(), request.getNarration());
        voucher.setReferenceNumber(request.getReferenceNumber());
        voucher.setVoucherIndex(generateNextVoucherNumber(VoucherType.JOURNAL));

        voucher = voucherRepository.save(voucher);

        // Create transaction entries
        List<TransactionEntry> entries = createTransactionEntries(voucher, request.getTransactionEntries());
        voucher.setTransactionEntries(entries);

        // Calculate totals
        calculateVoucherTotals(voucher);

        // Validate double-entry principle
        if (!voucher.isBalanced()) {
            throw new RuntimeException("Journal voucher is not balanced. Debit total must equal credit total.");
        }

        voucher = voucherRepository.save(voucher);

        log.info("Successfully processed Journal Voucher with ID: {}", voucher.getVoucherId());
        return voucher;
    }

    @Override
    public Voucher processCreditNoteVoucher(CreditNoteVoucherRequest request) {
        log.info("Processing Credit Note Voucher");

        Voucher voucher = new Voucher(VoucherType.CREDIT_NOTE, request.getVoucherDate(), request.getNarration());
        voucher.setReferenceNumber(request.getReferenceNumber());
        voucher.setVoucherIndex(generateNextVoucherNumber(VoucherType.CREDIT_NOTE));

        voucher = voucherRepository.save(voucher);

        // Create credit note entries
        List<TransactionEntryRequest> entries = createCreditNoteEntries(request);
        List<TransactionEntry> transactionEntries = createTransactionEntries(voucher, entries);
        voucher.setTransactionEntries(transactionEntries);

        // Calculate totals
        calculateVoucherTotals(voucher);

        voucher = voucherRepository.save(voucher);

        log.info("Successfully processed Credit Note Voucher with ID: {}", voucher.getVoucherId());
        return voucher;
    }

    @Override
    public Voucher processDebitNoteVoucher(DebitNoteVoucherRequest request) {
        log.info("Processing Debit Note Voucher");

        Voucher voucher = new Voucher(VoucherType.DEBIT_NOTE, request.getVoucherDate(), request.getNarration());
        voucher.setReferenceNumber(request.getReferenceNumber());
        voucher.setVoucherIndex(generateNextVoucherNumber(VoucherType.DEBIT_NOTE));

        voucher = voucherRepository.save(voucher);

        // Create debit note entries
        List<TransactionEntryRequest> entries = createDebitNoteEntries(request);
        List<TransactionEntry> transactionEntries = createTransactionEntries(voucher, entries);
        voucher.setTransactionEntries(transactionEntries);

        // Calculate totals
        calculateVoucherTotals(voucher);

        voucher = voucherRepository.save(voucher);

        log.info("Successfully processed Debit Note Voucher with ID: {}", voucher.getVoucherId());
        return voucher;
    }

    @Override
    public Voucher processRecurringVoucher(RecurringVoucherRequest request) {
        log.info("Processing Recurring Voucher");

        Voucher voucher = new Voucher(VoucherType.RECURRING, request.getVoucherDate(), request.getNarration());
        voucher.setReferenceNumber(request.getReferenceNumber());
        voucher.setIsRecurring(true);
        voucher.setRecurringFrequency(request.getRecurringFrequency());
        voucher.setRecurringEndDate(request.getRecurringEndDate());
        voucher.setNextRecurringDate(calculateNextRecurringDate(request.getVoucherDate(), request.getRecurringFrequency()));
        voucher.setVoucherIndex(generateNextVoucherNumber(VoucherType.RECURRING));

        voucher = voucherRepository.save(voucher);

        // Create transaction entries
        List<TransactionEntry> entries = createTransactionEntries(voucher, request.getTransactionEntries());
        voucher.setTransactionEntries(entries);

        // Calculate totals
        calculateVoucherTotals(voucher);

        voucher = voucherRepository.save(voucher);

        log.info("Successfully processed Recurring Voucher with ID: {}", voucher.getVoucherId());
        return voucher;
    }

    @Override
    public List<TransactionEntry> createTransactionEntries(Voucher voucher, List<TransactionEntryRequest> entryRequests) {
        List<TransactionEntry> entries = new ArrayList<>();

        for (TransactionEntryRequest entryRequest : entryRequests) {
            Ledger ledger = ledgerRepository.findById(entryRequest.getLedgerId())
                    .orElseThrow(() -> new LedgerNotFoundException("Ledger not found with ID: " + entryRequest.getLedgerId()));

            TransactionEntry entry = new TransactionEntry(
                    voucher,
                    ledger,
                    entryRequest.getEntryType(),
                    entryRequest.getAmount(),
                    entryRequest.getDescription()
            );

            entry.setReferenceNumber(entryRequest.getReferenceNumber());
            entry.setChequeNumber(entryRequest.getChequeNumber());

            entries.add(transactionEntryRepository.save(entry));
        }

        return entries;
    }

    @Override
    public boolean validateDoubleEntry(List<TransactionEntryRequest> entries) {
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;

        for (TransactionEntryRequest entry : entries) {
            if (entry.getEntryType() == EntryType.DEBIT) {
                debitTotal = debitTotal.add(entry.getAmount());
            } else {
                creditTotal = creditTotal.add(entry.getAmount());
            }
        }

        return debitTotal.compareTo(creditTotal) == 0;
    }

    @Override
    public Voucher postVoucher(Long voucherId) {
        log.info("Posting voucher with ID: {}", voucherId);

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new VoucherNotFound("Voucher not found with ID: " + voucherId));

        if (!voucher.canPost()) {
            throw new RuntimeException("Voucher cannot be posted. Status: " + voucher.getVoucherStatus());
        }

        voucher.setVoucherStatus(VoucherStatus.POSTED);
        voucher = voucherRepository.save(voucher);

        // Update ledger balances
        updateLedgerBalances(voucher);

        log.info("Successfully posted voucher with ID: {}", voucherId);
        return voucher;
    }

    @Override
    public Voucher reverseVoucher(Long voucherId, String reason) {
        log.info("Reversing voucher with ID: {}", voucherId);

        Voucher originalVoucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new VoucherNotFound("Voucher not found with ID: " + voucherId));

        if (originalVoucher.getVoucherStatus() != VoucherStatus.POSTED) {
            throw new RuntimeException("Only posted vouchers can be reversed");
        }

        // Create reversal voucher
        Voucher reversalVoucher = new Voucher(originalVoucher.getVoucherType(), LocalDate.now(), 
                "Reversal of: " + originalVoucher.getNarration() + " - Reason: " + reason);
        reversalVoucher.setVoucherIndex(generateNextVoucherNumber(originalVoucher.getVoucherType()) + "-R");
        reversalVoucher.setIsReversed(true);
        reversalVoucher.setReversedVoucherId(voucherId);

        reversalVoucher = voucherRepository.save(reversalVoucher);

        // Create reversed transaction entries
        List<TransactionEntry> reversedEntries = new ArrayList<>();
        for (TransactionEntry originalEntry : originalVoucher.getTransactionEntries()) {
            TransactionEntry reversedEntry = new TransactionEntry(
                    reversalVoucher,
                    originalEntry.getLedger(),
                    originalEntry.getEntryType().getOpposite(),
                    originalEntry.getAmount(),
                    "Reversal: " + originalEntry.getDescription()
            );
            reversedEntries.add(transactionEntryRepository.save(reversedEntry));
        }

        reversalVoucher.setTransactionEntries(reversedEntries);
        calculateVoucherTotals(reversalVoucher);
        reversalVoucher.setVoucherStatus(VoucherStatus.POSTED);
        reversalVoucher = voucherRepository.save(reversalVoucher);

        // Mark original voucher as reversed
        originalVoucher.setIsReversed(true);
        originalVoucher.setReversedVoucherId(reversalVoucher.getVoucherId());
        voucherRepository.save(originalVoucher);

        log.info("Successfully reversed voucher with ID: {} using reversal voucher ID: {}", voucherId, reversalVoucher.getVoucherId());
        return reversalVoucher;
    }

    @Override
    public Voucher approveVoucher(Long voucherId) {
        log.info("Approving voucher with ID: {}", voucherId);

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new VoucherNotFound("Voucher not found with ID: " + voucherId));

        if (voucher.getVoucherStatus() != VoucherStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Only vouchers pending approval can be approved");
        }

        voucher.setVoucherStatus(VoucherStatus.APPROVED);
        voucher = voucherRepository.save(voucher);

        log.info("Successfully approved voucher with ID: {}", voucherId);
        return voucher;
    }

    @Override
    public Voucher rejectVoucher(Long voucherId, String reason) {
        log.info("Rejecting voucher with ID: {}", voucherId);

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new VoucherNotFound("Voucher not found with ID: " + voucherId));

        if (voucher.getVoucherStatus() != VoucherStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Only vouchers pending approval can be rejected");
        }

        voucher.setVoucherStatus(VoucherStatus.REJECTED);
        voucher.setNarration(voucher.getNarration() + " [REJECTED: " + reason + "]");
        voucher = voucherRepository.save(voucher);

        log.info("Successfully rejected voucher with ID: {} with reason: {}", voucherId, reason);
        return voucher;
    }

    @Override
    @Transactional(readOnly = true)
    public Voucher getVoucherWithEntries(Long voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new VoucherNotFound("Voucher not found with ID: " + voucherId));

        // Load transaction entries
        List<TransactionEntry> entries = transactionEntryRepository.findByVoucherOrderByEntryIdAsc(voucher);
        voucher.setTransactionEntries(entries);

        return voucher;
    }

    @Override
    public String generateNextVoucherNumber(VoucherType voucherType) {
        return voucherService.getFormattedVoucherId(voucherService.generateFormattedVoucherId(voucherType));
    }

    // Private helper methods

    private void validateContraVoucher(ContraVoucherRequest request) {
        if (request.getTransactionEntries().size() != 2) {
            throw new RuntimeException("Contra voucher must have exactly 2 entries");
        }

        boolean hasDebit = false;
        boolean hasCredit = false;

        for (TransactionEntryRequest entry : request.getTransactionEntries()) {
            if (entry.getEntryType() == EntryType.DEBIT) {
                hasDebit = true;
            } else {
                hasCredit = true;
            }
        }

        if (!hasDebit || !hasCredit) {
            throw new RuntimeException("Contra voucher must have one debit and one credit entry");
        }

        if (!validateDoubleEntry(request.getTransactionEntries())) {
            throw new RuntimeException("Contra voucher entries are not balanced");
        }
    }

    private List<TransactionEntryRequest> createCreditNoteEntries(CreditNoteVoucherRequest request) {
        List<TransactionEntryRequest> entries = new ArrayList<>();

        // Debit: Customer Account
        TransactionEntryRequest debitEntry = TransactionEntryRequest.builder()
                .ledgerId(request.getCustomerLedgerId())
                .entryType(EntryType.DEBIT)
                .amount(request.getAmount())
                .description("Credit Note - " + request.getReason())
                .build();
        entries.add(debitEntry);

        // Credit: Sales Account (or adjustment account)
        // For now, we'll use a default sales adjustment account
        // In a real system, you'd have logic to determine the appropriate credit account
        TransactionEntryRequest creditEntry = TransactionEntryRequest.builder()
                .ledgerId(1L) // Default sales account - should be configurable
                .entryType(EntryType.CREDIT)
                .amount(request.getAmount())
                .description("Credit Note - " + request.getReason())
                .build();
        entries.add(creditEntry);

        return entries;
    }

    private List<TransactionEntryRequest> createDebitNoteEntries(DebitNoteVoucherRequest request) {
        List<TransactionEntryRequest> entries = new ArrayList<>();

        // Debit: Purchase Account (or adjustment account)
        TransactionEntryRequest debitEntry = TransactionEntryRequest.builder()
                .ledgerId(1L) // Default purchase account - should be configurable
                .entryType(EntryType.DEBIT)
                .amount(request.getAmount())
                .description("Debit Note - " + request.getReason())
                .build();
        entries.add(debitEntry);

        // Credit: Vendor Account
        TransactionEntryRequest creditEntry = TransactionEntryRequest.builder()
                .ledgerId(request.getVendorLedgerId())
                .entryType(EntryType.CREDIT)
                .amount(request.getAmount())
                .description("Debit Note - " + request.getReason())
                .build();
        entries.add(creditEntry);

        return entries;
    }

    private LocalDate calculateNextRecurringDate(LocalDate currentDate, String frequency) {
        switch (frequency.toUpperCase()) {
            case "DAILY":
                return currentDate.plusDays(1);
            case "WEEKLY":
                return currentDate.plusWeeks(1);
            case "MONTHLY":
                return currentDate.plusMonths(1);
            case "YEARLY":
                return currentDate.plusYears(1);
            default:
                return currentDate.plusMonths(1); // Default to monthly
        }
    }

    private void calculateVoucherTotals(Voucher voucher) {
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;

        for (TransactionEntry entry : voucher.getTransactionEntries()) {
            if (entry.getEntryType() == EntryType.DEBIT) {
                debitTotal = debitTotal.add(entry.getAmount());
            } else {
                creditTotal = creditTotal.add(entry.getAmount());
            }
        }

        voucher.setDebitTotal(debitTotal);
        voucher.setCreditTotal(creditTotal);
        voucher.setTotalAmount(debitTotal); // Total amount is same as debit total for balanced vouchers
    }

    private void updateLedgerBalances(Voucher voucher) {
        for (TransactionEntry entry : voucher.getTransactionEntries()) {
            Ledger ledger = entry.getLedger();
            BigDecimal currentBalance = ledger.getCurrentBalance();

            if (entry.getEntryType() == EntryType.DEBIT) {
                if (ledger.getAccountGroup().getGroupType().isDebitNormal()) {
                    ledger.setCurrentBalance(currentBalance.add(entry.getAmount()));
                } else {
                    ledger.setCurrentBalance(currentBalance.subtract(entry.getAmount()));
                }
            } else {
                if (ledger.getAccountGroup().getGroupType().isCreditNormal()) {
                    ledger.setCurrentBalance(currentBalance.add(entry.getAmount()));
                } else {
                    ledger.setCurrentBalance(currentBalance.subtract(entry.getAmount()));
                }
            }

            ledgerRepository.save(ledger);
        }
    }
}
