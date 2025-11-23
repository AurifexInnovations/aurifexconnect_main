package com.erp.Dto.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {

    private Long billId;
    private Long vendorId;
    private String datePaid;
    private BigDecimal amountPaid;
    private Long voucherId;
    private String paymentMethod;
    private String notes;
}
