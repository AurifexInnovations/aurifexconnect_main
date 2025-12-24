package com.erp.Dto.Request;

import com.erp.Enum.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceiptRequestDto {

    private Long paymentId;
    private Long invoiceId;
    private Long customerId;
    private Long branchId;

    private String receiptNumber;
    private BigDecimal amountReceived;
    private PaymentMethod paymentMethod;
    private String notes;
}
