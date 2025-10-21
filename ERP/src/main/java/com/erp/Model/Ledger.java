package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
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

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    private String GSTno;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "ledger")
    private List<Master> masters;

    @OneToMany(mappedBy = "ledger")
    private List<AgainstRefMap> againstRefMaps;



    @Column(name = "creditlimit", nullable = false)
    private double creditLimit = 0.00;

    @Column(name = "debitlimit", nullable = false)
    private double debitLimit = 0.00;


//    @OneToOne(mappedBy = "ledger")
//    private  InvoiceGenerator invoiceGenerator;
}