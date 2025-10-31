package com.erp.Model;

import com.erp.Enum.ReconciliationStatus;
import com.erp.Enum.TransactionType;
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

@Entity
@Table(name = "bank_statement_line")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BankStatementLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long lineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statement_id", nullable = false)
    private BankStatement bankStatement;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "value_date")
    private LocalDate valueDate;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "cheque_number")
    private String chequeNumber;

    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType; // DEBIT, CREDIT

    @Enumerated(EnumType.STRING)
    @Column(name = "reconciliation_status")
    private ReconciliationStatus reconciliationStatus = ReconciliationStatus.UNRECONCILED;

    @Column(name = "balance_after", precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(name = "notes")
    private String notes;

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

    // Reconciliation fields
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_transaction_entry_id")
    private TransactionEntry matchedTransactionEntry;

    @Column(name = "reconciliation_date")
    private LocalDateTime reconciliationDate;

    @Column(name = "reconciled_by")
    private String reconciledBy;

    // Constructors
    public BankStatementLine() {}

    public BankStatementLine(BankStatement bankStatement, LocalDate transactionDate, String description, 
                           BigDecimal amount, TransactionType transactionType) {
        this.bankStatement = bankStatement;
        this.transactionDate = transactionDate;
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
        this.reconciliationStatus = ReconciliationStatus.UNRECONCILED;
    }
}
