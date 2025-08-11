package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class InvoiceGenerator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_generated_id")
    private long invoiceGeneratedId;

    @OneToOne
    private Master master;

    @CreatedDate
    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @ManyToOne
    private Admin admin;
}
