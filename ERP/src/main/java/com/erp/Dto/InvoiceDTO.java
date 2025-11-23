package com.erp.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDTO {
    private Integer invoiceId;
    private Integer soId;
    private Integer customerId;
    private LocalDate date;
    private Double totalAmount;
    private Integer taxId;
    private String pendingStatus;
    private String invoiceNumber;
}