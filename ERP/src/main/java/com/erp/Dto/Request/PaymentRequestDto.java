package com.erp.Dto.Request;

import com.erp.Enum.PaymentMethod;
import com.erp.Enum.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {

    private Long invoiceId;
    private Long customerId;

    private BigDecimal invoiceAmount;
    private BigDecimal amountPaid;

    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;

    private String transactionReference;
    private LocalDateTime paymentDate;
    private String notes;
}
