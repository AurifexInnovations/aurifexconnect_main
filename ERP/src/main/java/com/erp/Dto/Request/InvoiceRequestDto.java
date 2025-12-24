package com.erp.Dto.Request;

import com.erp.Enum.InvoiceStatus;
import com.erp.Enum.InvoiceType;
import com.erp.Enum.ServiceCategory;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequestDto {


    private Long customerId;

    private Long salesOrderId;

    private Long branchId;

   // private Long quotationId;

   // private LocalDate invoiceDate;

    private LocalDate dueDate;

    private ServiceCategory serviceCategory;

    private BigDecimal sqft;

    private InvoiceType invoiceIsFor;

    private InvoiceStatus status;

    private String PaymentStatus;

    private String notes;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    private BigDecimal grandTotal;

    private BigDecimal amountPaid = BigDecimal.ZERO;

    private BigDecimal balanceAmount = BigDecimal.ZERO;


}
