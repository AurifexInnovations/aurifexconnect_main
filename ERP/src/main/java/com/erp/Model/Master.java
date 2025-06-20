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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Master {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long masterId;

    private String name;

    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;

    private double amount;

    private double taxAmount;

    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private String description;

    private String voucherIndex;

    @CreatedDate
    private LocalDateTime createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
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
    private  InvoiceGenerator invoiceGenerator;

    @ManyToOne
    private Master referenceMaster;
}
