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


}
