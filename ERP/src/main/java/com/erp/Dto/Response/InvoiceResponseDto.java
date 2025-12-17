package com.erp.Dto.Response;

import com.erp.Enum.InvoiceStatus;
import com.erp.Enum.InvoiceType;
import com.erp.Enum.ServiceCategory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor

public class InvoiceResponseDto {

    private Long id;
    private Long customerId;
    private Long salesOrderId;
    private Long quotationId;

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private LocalDate dueDate;

    private ServiceCategory serviceCategory;
    private BigDecimal sqft;
    private InvoiceType invoiceIsFor;

    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal grandTotal;

    private BigDecimal amountPaid;
    private BigDecimal balanceAmount;

    private InvoiceStatus status;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
