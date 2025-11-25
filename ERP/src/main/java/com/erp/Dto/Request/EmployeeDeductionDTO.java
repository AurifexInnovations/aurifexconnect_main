package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EmployeeDeductionDTO {
    private Long employeeId;
    private String deductionType;
    private BigDecimal amount;
}
