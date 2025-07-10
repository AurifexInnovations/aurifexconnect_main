package com.erp.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(EntityListeners.class)
public class InvoiceGenerator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invoiceGeneratedId;

    private double totalAmount;

    @OneToOne
    private Master master;

    @CreatedDate
    private LocalDateTime generatedAt;

    @ManyToOne
    private Admin admin;

}
