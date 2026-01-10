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
public class JournalDTO {
    private Integer journalId;
    private LocalDate date;
    private Integer voucherId;
    private String referenceType;
    private Integer referenceId;
    private String status;
    private String description;
}