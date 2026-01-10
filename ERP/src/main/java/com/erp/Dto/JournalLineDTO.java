package com.erp.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalLineDTO {
    private Integer lineId;
    private Integer journalId;
    private Integer coaId;
    private Double debit;
    private Double credit;
    private String lineDescription;
}