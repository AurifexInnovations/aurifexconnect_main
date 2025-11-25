package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdatePayslipRequestDTO {
    private List<EmployeeDeductionDTO> employeeDeductions;
}
