package com.erp.Projection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface InvoiceProjection {

    Long getInvoiceId();
    Long getCustomerId();
    Long getSalesOrderId();
    Long getQuotationId();
    String getInvoiceNumber();
    LocalDate getInvoiceDate();
    LocalDate getDueDate();
    String getServiceCategory();
    BigDecimal getSqft();
    String getInvoiceIsFor();
    BigDecimal getSubtotal();
    BigDecimal getTaxAmount();
    BigDecimal getTotalAmount();
    BigDecimal getDiscountAmount();
    BigDecimal getGrandTotal();
    BigDecimal getAmountPaid();
    BigDecimal getBalanceAmount();
    String getStatus();
    String getNotes();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    String getCustomerName();
}
