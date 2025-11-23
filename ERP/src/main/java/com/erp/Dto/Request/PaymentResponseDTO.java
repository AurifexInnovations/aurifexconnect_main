package com.erp.Dto.Request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentResponseDTO {

    private Long paymentId;
    private Long billId;
    private Long vendorId;
    private LocalDate datePaid;
    private BigDecimal amountPaid;
    private Long voucherId;
    private String paymentMethod;
    private String paymentNumber;
    private String notes;
    private LocalDate createdAt;
    private Boolean isActive;
    private LocalDate createdDate;
    private LocalDate updatedDate;
}
