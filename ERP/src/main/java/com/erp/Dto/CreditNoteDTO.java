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
public class CreditNoteDTO {
    private Integer cnId;
    private Integer invoiceId;
    private LocalDate date;
    private Double amount;
    private String reason;
    private String status;
    private String cnNumber;
}