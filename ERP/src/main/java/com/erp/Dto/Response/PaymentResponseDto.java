package com.erp.Dto.Response;

import com.erp.Enum.PaymentMethod;
import com.erp.Enum.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {

    private Long id;

    private Long invoiceId;
    private Long customerId;
    private Long branchId;

    private BigDecimal invoiceAmount;
    private BigDecimal amountPaid;
    private BigDecimal totalPaidTillNow;
    private BigDecimal balanceAmount;

    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;

    private String transactionReference;
    private LocalDateTime paymentDate;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
