package com.erp.Dto.Request;

import com.erp.Enum.ContractStatus;
import com.erp.Enum.ServiceFrequency;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractRequestDto {
    private Long id ;
    private Long customerId;
    private Long quotationId;
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
    private Long createdBy;
    private Long lastModifiedBy;
}
