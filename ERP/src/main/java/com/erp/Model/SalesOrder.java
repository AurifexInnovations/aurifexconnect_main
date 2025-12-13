package com.erp.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_orders")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quotation_id")
    private Long quotationId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "sales_order_number", nullable = false, unique = true)
    private String salesOrderNumber;

    @Column(name = "sales_order_date")
    private LocalDate salesOrderDate;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "company_name", length = 150)
    private String companyName;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "alternate_phone_number", length = 20)
    private String alternatePhoneNumber;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "location_url")
    private String locationUrl;

    @Column(name = "service_category", length = 20)
    private String serviceCategory;

    @Column(name = "sqft", precision = 10, scale = 2)
    private BigDecimal sqft;

    @Column(name = "so_type", nullable = false, length = 20)
    private String soType;

    @Column(name = "items", columnDefinition = "jsonb", nullable = false)
    private String items;

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters
}
