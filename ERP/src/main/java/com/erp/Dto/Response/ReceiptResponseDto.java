package com.erp.Dto.Response;

import com.erp.Enum.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReceiptResponseDto {

    private Long id;
    private Long paymentId;
    private Long invoiceId;
    private Long customerId;
    private Long branchId;

    private String receiptNumber;
    private LocalDateTime receiptDate;

    private BigDecimal amountReceived;
    private PaymentMethod paymentMethod;
    private String notes;

    private LocalDateTime createdAt;
}
