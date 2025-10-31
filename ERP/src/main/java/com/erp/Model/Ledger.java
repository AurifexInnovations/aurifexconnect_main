package com.erp.Model;

import com.erp.Enum.BalanceType;
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
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ledger")
@EntityListeners(AuditingEntityListener.class)
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ledger_id")
    private long ledgerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "ledger_code", unique = true, length = 20)
    private String ledgerCode;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "gst_no")
    private String GSTno;

    @Column(name = "pan_no")
    private String panNo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "opening_balance", precision = 19, scale = 2)
    private BigDecimal openingBalance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "balance_type")
    private BalanceType balanceType = BalanceType.DEBIT;

    @Column(name = "credit_limit", precision = 19, scale = 2)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(name = "debit_limit", precision = 19, scale = 2)
    private BigDecimal debitLimit = BigDecimal.ZERO;

    @Column(name = "current_balance", precision = 19, scale = 2)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // CoA Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private AccountGroup accountGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subgroup_id")
    private AccountSubGroup accountSubGroup;

    // Existing relationships
    @OneToMany(mappedBy = "ledger")
    private List<Master> masters;

    @OneToMany(mappedBy = "ledger")
    private List<AgainstRefMap> againstRefMaps;

//    @OneToOne(mappedBy = "ledger")
//    private  InvoiceGenerator invoiceGenerator;
}