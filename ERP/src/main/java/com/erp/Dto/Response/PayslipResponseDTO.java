package com.erp.Dto.Response;

import com.erp.Dto.Request.EmployeeDeductionDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PayslipResponseDTO {

    private Long payslipId;
    private Long employeeId;

    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private LocalDate generationDate;

    private BigDecimal grossSalary;
    private BigDecimal totalDeductions;
    private BigDecimal netPay;

    private String status;
    private String payslipNumber;

    private Long voucherId;

    private List<EmployeeDeductionDTO> employeeDeductions;
}
