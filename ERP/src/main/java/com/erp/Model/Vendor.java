package com.erp.Model;

import com.erp.Enum.VendorStatus;
import com.erp.Enum.VendorType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Data
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_code", unique = true)
    private String vendorCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor_type")
    private VendorType vendorType;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    private String email;
    private String phone;

    @Column(name = "alternate_phone")
    private String alternatePhone;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    private String city;
    private String state;
    private String country;
    private String pincode;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "msme_number")
    private String msmeNumber;

    @Column(name = "gst_registered")
    private Boolean gstRegistered;

    @Column(name = "payment_terms_days")
    private Integer paymentTermsDays;

    @Column(name = "preferred_currency")
    private String preferredCurrency;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "bank_ifsc_code")
    private String bankIfscCode;

    @Enumerated(EnumType.STRING)
    private VendorStatus status;

    private Integer rating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
