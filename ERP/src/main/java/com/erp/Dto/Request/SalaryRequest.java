package com.erp.Dto.Request;

import lombok.*;
import java.time.YearMonth;

@Getter
@Setter
public class SalaryRequest {
    private long userId;
    private YearMonth month;
    private long baseSalary;
    private long bonus;
    private long deductions;
    private Integer workingDays;
    private Integer paidDays;
    private String remarks;
}
