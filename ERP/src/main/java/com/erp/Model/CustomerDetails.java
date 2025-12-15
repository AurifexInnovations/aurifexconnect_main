package com.erp.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CustomerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "tags")
    private String tags;

    @Column(name = "customer_status")
    private String customerStatus;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // ALTERS
    @Column(name = "alternate_phone")
    private String alternatePhone;

    @Column(name = "location_url")
    private String locationUrl;

    @Column(name = "customer_type")
    private String customerType;

    @Column(name = "service_category")
    private String serviceCategory;

    @Column(name = "sqrt")
    private Double sqrt;

    @Column(name = "total_quotation")
    private Long totalQuotation;

    @Column(name = "total_sales_order")
    private Long totalSalesOrder;

    @Column(name = "total_invoices")
    private Long totalInvoices;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}
