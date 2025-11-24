package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "sales_orders")
@Getter
@Setter
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_id")
    private Long soId;

    @Column(name = "quotation_id")
    private Long quotationId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "status")
    private String status = "Draft";

    @Column(name = "total_value")
    private BigDecimal totalValue;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "delivery_terms")
    private String deliveryTerms;

    @Column(name = "sales_notes")
    private String salesNotes;

    @Column(name = "is_active")
    private Boolean isActive = true;


    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
