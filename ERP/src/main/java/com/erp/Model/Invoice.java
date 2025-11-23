package com.erp.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invoiceId;

    @Column(name = "so_id")
    private Integer soId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private Double totalAmount;

    @Column(name = "tax_id")
    private Integer taxId;

    @Column(name = "pending_status", nullable = false, length = 20)
    private String pendingStatus;

    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "updated_by")
    private Integer updatedBy;
}