package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SalaryAnalyticsResponse {
    private double totalAmountPaid;
    private List<SalarySummaryResponse> monthlyBreakdown;
    private List<MonthlySalaryResponse> yearWiseBreakdown;
}
