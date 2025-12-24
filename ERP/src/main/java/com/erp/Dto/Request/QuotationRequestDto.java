package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class QuotationRequestDto {

    // IDs
    private Long id;
    private Long leadId;
    private Long customerId;
    private Long branchId;

    // Personal / Company
    private String fullName;
    private String companyName;
    private String email;
    private String phone;
    private String alternatePhone;

    // Address
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String locationUrl;

    // Quotation core
    private String quotationNumber;
   // private LocalDate quotationDate;

    // Category / size
    private String serviceType;   // RESIDENTIAL / COMMERCIAL
    private Double sqrt;

    // Line items
    private List<QuotationProductRequestDto> products;
    private List<Long> services;

    // Financials
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal grandTotal;

    // Lead type (🔥 ADDED)
    private String quotationType;   // PRODUCT / SERVICE

    // Status
    private String status;   // DRAFT / SENT / ACCEPTED / REJECTED / EXPIRED / CONVERTED

    // Sent info
    private LocalDateTime sentDate;
    private String sentVia;   // EMAIL / WHATSAPP / MANUAL

    // Recurring
    private Boolean isRecurring;
    private String recurringType;      // MONTHLY / YEARLY
    private Integer recurringInterval;
    private Integer recurringCycles;
    private LocalDate startDate;
    private LocalDate nextRecurringDate;
    private LocalDate endDate;

    // Notes
    private String notes;

    // Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
