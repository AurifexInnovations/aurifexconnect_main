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
public class ReceiptDTO {
    private Integer receiptId;
    private Integer invoiceId;
    private LocalDate date;
    private Double amount;
    private Integer voucherId;
    private String paymentMethod;
    private String notes;
}