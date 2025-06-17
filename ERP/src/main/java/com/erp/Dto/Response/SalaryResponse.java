package com.erp.Dto.Response;

import java.time.YearMonth;
import com.erp.Enum.AmountStatus;
import lombok.*;

@Getter
@Setter
public class SalaryResponse {
    private long id;
    private UserResponse user;
    private String firstName;
    private String lastName;
    private YearMonth month;
    private long baseSalary;
    private long deductions;
    private long bonus;
    private Integer workingDays;
    private Integer paidDays;
    private long netSalary;
    private String remarks;
    private AmountStatus amountStatus;
    private YearMonth paymentDate;
}