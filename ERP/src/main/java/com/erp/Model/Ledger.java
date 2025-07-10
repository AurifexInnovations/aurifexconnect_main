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
@Table(name = "Ledger")
@EntityListeners(AuditingEntityListener.class)
public class Ledger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ledgerId;

    private String name;
    private String email;
    private String phone;
    private String address;

    @CreatedDate
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "ledger")
    private List<Master> masters;

    @OneToMany(mappedBy = "ledger")
    private List<AgainstRefMap> againstRefMaps;

    @OneToOne(mappedBy = "ledger")
    private  InvoiceGenerator invoiceGenerator;
}