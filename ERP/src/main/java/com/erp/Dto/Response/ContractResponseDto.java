package com.erp.Dto.Response;

import com.erp.Enum.ContractStatus;
import com.erp.Enum.ServiceFrequency;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponseDto {
    private Long id;
    private String customerId;
    private String quotationId;
    private ContractStatus contractStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalValue;
    private ServiceFrequency serviceFrequency;
    private String paymentTerms;
    private Boolean isRecurring;
    private LocalDate activationDate;
    private LocalDate renewalDate;
    private String contractNotes;
    private String createdBy;
    private LocalDateTime createdAt;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedAt;
}
