package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class QuotationRequestDto {

    private Long id; // for update

    // Lead / Customer reference
    private Long leadId;
    private Long customerId;

    // Basic details (same pattern as Customer)
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

    // Quotation specific
    private String quotationNumber;
    private LocalDate quotationDate;

    private String serviceCategory;   // RESIDENTIAL / COMMERCIAL
    private Double sqrt;

    // Products (same pattern as Customer products)
    private List<QuotationProductRequestDto> products;

    // Services (same pattern as Customer services)
    private List<Long> services;

    // Amounts
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal grandTotal;

    // Status
    private String status; // DRAFT / SENT / ACCEPTED / REJECTED

    // Recurring
    private Boolean isRecurring;
    private String recurringType;     // MONTHLY / YEARLY
    private Integer recurringInterval;
    private Integer recurringCycles;
    private LocalDate startDate;
    private LocalDate nextRecurringDate;
    private LocalDate endDate;

    // Other
    private String notes;

}
