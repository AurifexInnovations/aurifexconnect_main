package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreatePayslipRequestDTO {

    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private LocalDate generationDate;
    private Long voucherId;

    private List<EmployeeDeductionDTO> employeeDeductions;
}
