package com.erp.Model;

import com.erp.Enum.AccountStatus;
import com.erp.Enum.Banks;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bank_account")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    private Long bankAccountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_name")
    private Banks bankName;

    @Column(name = "opening_balance")
    private double openingBalance;

    @Column(name = "current_balance")
    private double currentBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus accountStatus;

    @CreatedBy
    @Column(name = "created_by")
    private String CreatedBy;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @OneToOne
    private Ledger ledger;

    @OneToMany(mappedBy = "bankAccount")
    private List<Master> masters;
}