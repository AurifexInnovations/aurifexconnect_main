package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
public class QuotationResponse {

    private Long id;
    private String quotationId;
    private String type;
    private Long customerId;
    private String address;
    private String contactPerson;
    private LocalDate quotationDate;
    private LocalDate validityDate;
    private String paymentTerms;
    private Double totalAmount;
    private String status;
    private String notes;
    private String language;
    private LocalDateTime createdAt;

    // Getters and Setters
}
