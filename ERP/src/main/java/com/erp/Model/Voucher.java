package com.erp.Model;

import com.erp.Enum.VoucherStatus;
import com.erp.Enum.VoucherType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "voucher")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long voucherId;

    @Enumerated(EnumType.STRING)
    @Column(name = "voucher_type", nullable = false)
    private VoucherType voucherType;

    @Column(name = "voucher_index", unique = true)
    private String voucherIndex;

    @Column(name = "voucher_date", nullable = false)
    private LocalDate voucherDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "voucher_status", nullable = false)
    private VoucherStatus voucherStatus = VoucherStatus.DRAFT;

    @Column(name = "total_amount", precision = 19, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "debit_total", precision = 19, scale = 2)
    private BigDecimal debitTotal = BigDecimal.ZERO;

    @Column(name = "credit_total", precision = 19, scale = 2)
    private BigDecimal creditTotal = BigDecimal.ZERO;

    @Column(name = "narration")
    private String narration;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "is_recurring")
    private Boolean isRecurring = false;

    @Column(name = "recurring_frequency")
    private String recurringFrequency; // DAILY, WEEKLY, MONTHLY, YEARLY

    @Column(name = "recurring_end_date")
    private LocalDate recurringEndDate;

    @Column(name = "next_recurring_date")
    private LocalDate nextRecurringDate;

    @Column(name = "is_reversed")
    private Boolean isReversed = false;

    @Column(name = "reversed_voucher_id")
    private Long reversedVoucherId;

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

    // Relationships
    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Master> masters;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionEntry> transactionEntries;

    // Legacy fields for backward compatibility
    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // Constructors
    public Voucher() {}

    public Voucher(VoucherType voucherType, LocalDate voucherDate, String narration) {
        this.voucherType = voucherType;
        this.voucherDate = voucherDate;
        this.narration = narration;
        this.voucherStatus = VoucherStatus.DRAFT;
    }

    /**
     * Validate double-entry principle
     */
    public boolean isBalanced() {
        return debitTotal.compareTo(creditTotal) == 0;
    }

    /**
     * Check if voucher can be edited
     */
    public boolean canEdit() {
        return voucherStatus.canEdit();
    }

    /**
     * Check if voucher can be posted
     */
    public boolean canPost() {
        return voucherStatus.canPost() && isBalanced();
    }
}
