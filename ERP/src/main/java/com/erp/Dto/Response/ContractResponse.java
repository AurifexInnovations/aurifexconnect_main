package com.erp.Dto.Response;

import com.erp.Enum.ContractStatus;
import com.erp.Enum.ServiceFrequency;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ContractResponse {
    private Long id;
    private String customerName;
    private LocalDateTime createdAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private ServiceFrequency serviceFrequency;
    private BigDecimal totalValue;
    private ContractStatus contractStatus;
}
