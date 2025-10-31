package com.erp.Model;

import com.erp.Enum.StatementStatus;
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
@Table(name = "bank_statement")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BankStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statement_id")
    private Long statementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "statement_date", nullable = false)
    private LocalDate statementDate;

    @Column(name = "opening_balance", precision = 19, scale = 2)
    private BigDecimal openingBalance;

    @Column(name = "closing_balance", precision = 19, scale = 2)
    private BigDecimal closingBalance;

    @Column(name = "total_debits", precision = 19, scale = 2)
    private BigDecimal totalDebits = BigDecimal.ZERO;

    @Column(name = "total_credits", precision = 19, scale = 2)
    private BigDecimal totalCredits = BigDecimal.ZERO;

    @Column(name = "statement_reference")
    private String statementReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "statement_status")
    private StatementStatus statementStatus = StatementStatus.IMPORTED;

    @Column(name = "import_source")
    private String importSource; // CSV, EXCEL, API, MANUAL

    @Column(name = "import_file_name")
    private String importFileName;

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

    @OneToMany(mappedBy = "bankStatement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BankStatementLine> statementLines;

    // Constructors
    public BankStatement() {}

    public BankStatement(BankAccount bankAccount, LocalDate statementDate, BigDecimal openingBalance, BigDecimal closingBalance) {
        this.bankAccount = bankAccount;
        this.statementDate = statementDate;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
        this.statementStatus = StatementStatus.IMPORTED;
    }
}
