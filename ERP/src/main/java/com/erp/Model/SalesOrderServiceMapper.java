package com.erp.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "sales_order_service_mapper")
@Data
public class SalesOrderServiceMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // auto-generated

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "sales_order_id", nullable = false)
    private Long salesOrderId;

    @Column(name = "quantity", precision = 12, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;
}
