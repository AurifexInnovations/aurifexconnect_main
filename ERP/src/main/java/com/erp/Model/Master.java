package com.erp.Model;

import com.erp.Enum.ReferenceType;
import com.erp.Enum.TransactionStatus;
import com.erp.Enum.VoucherType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Master {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id")
    private long masterId;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "voucher_type")
    private VoucherType voucherType;

    @Column(name = "amount")
    private double amount;

    @Column(name = "tax_amount")
    private double taxAmount;

    @Column(name = "total_amount")
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @Column(name = "description")
    private String description;

    @Column(name = "voucher_index")
    private String voucherIndex;

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

    @ManyToOne
    private BankAccount bankAccount;

    @ManyToOne
    private Ledger ledger;

    @ManyToOne
    private Voucher voucher;

    @OneToMany(mappedBy = "master", fetch = FetchType.LAZY)
    private List<LineItems> lineItems;

    @OneToMany(mappedBy = "master")
    private List<AgainstRefMap> againstRefMaps;

    @OneToOne(mappedBy = "master")
    private InvoiceGenerator invoiceGenerator;

    @ManyToOne
    private Master referenceMaster;
}
