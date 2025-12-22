package com.erp.Model;

import com.erp.Enum.SalesOrderStatus;
import com.erp.Enum.SalesOrderType;
import com.erp.Enum.ServiceCategory;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_orders")
@Data
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_order_number")
    private Long salesOrderNumber;

    @Column(name = "quotation_id")
    private Long quotationId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;



    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "alternate_phone_number", length = 20)
    private String alternatePhoneNumber;

    @Column(name = "sales_order_date")
    private LocalDate salesOrderDate;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "company_name", length = 150)
    private String companyName;

    @Column(name = "email", length = 150)
    private String email;


    @Column(name = "address", columnDefinition = "TEXT")
    private String address;


    @Column(columnDefinition = "TEXT")
    private String landmark;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 10)
    private String pincode;

    @Column(name = "location_url", columnDefinition = "TEXT")
    private String locationUrl;


    @Column(name = "service_category", length = 20)
    private String serviceCategory;

    @Column(name = "sqft", precision = 10, scale = 2)
    private BigDecimal sqft;

    @Enumerated(EnumType.STRING)
    @Column(name = "sales_order_type", nullable = false, length = 20)
    private SalesOrderType soType;



    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private SalesOrderStatus status;




    @Column(columnDefinition = "TEXT")
    private String notes;


    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false, length = 30)
    private ServiceCategory serviceType;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;




}
