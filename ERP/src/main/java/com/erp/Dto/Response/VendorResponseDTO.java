package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class VendorResponseDTO {
    private Long vendorId;
    private String vendorName;
    private String contactPerson;
    private String phoneNumber;
    private String emailAddress;
    private String billingAddress;
    private String paymentTerms;
    private BigDecimal creditLimit;
    private Boolean isActive;
    private BigDecimal totalSpend;
    private BigDecimal outstandingPayables;
    private LocalDateTime updatedDate;
    private LocalDateTime createdDate;
}
