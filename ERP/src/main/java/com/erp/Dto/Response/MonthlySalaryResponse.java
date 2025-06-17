package com.erp.Dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySalaryResponse {
    private String month;
    private double amount;
}