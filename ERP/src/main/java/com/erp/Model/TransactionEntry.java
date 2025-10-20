package com.erp.Model;

import com.erp.Enum.BalanceType;
import com.erp.Enum.EntryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_entry")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class TransactionEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Long entryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_id", nullable = false)
    private Ledger ledger;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false)
    private EntryType entryType;

    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "cheque_number")
    private String chequeNumber;

    @Column(name = "cheque_date")
    private LocalDateTime chequeDate;

    @Column(name = "is_reconciled")
    private Boolean isReconciled = false;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    // Constructors
    public TransactionEntry() {}

    public TransactionEntry(Voucher voucher, Ledger ledger, EntryType entryType, BigDecimal amount, String description) {
        this.voucher = voucher;
        this.ledger = ledger;
        this.entryType = entryType;
        this.amount = amount;
        this.description = description;
    }

    /**
     * Get the balance type for this entry based on entry type and ledger
     */
    public BalanceType getEffectiveBalanceType() {
        if (entryType == EntryType.DEBIT) {
            return ledger.getAccountGroup().getGroupType().isDebitNormal() ? BalanceType.DEBIT : BalanceType.CREDIT;
        } else {
            return ledger.getAccountGroup().getGroupType().isCreditNormal() ? BalanceType.CREDIT : BalanceType.DEBIT;
        }
    }
}
