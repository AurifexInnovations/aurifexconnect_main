package com.erp.Service.SalaryService;

import com.erp.Dto.Request.Param;
import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.MonthlySalaryResponse;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.Dto.Response.SalarySummaryResponse;

import java.time.YearMonth;
import java.util.List;

public interface SalaryService {
    SalaryResponse generateSalaryForMonth(SalaryRequest request);
    List<SalaryResponse> getSalariesByUserId(Param param);
    SalaryResponse getSalaryByUserAndMonth(SalaryRequest request);
    SalaryResponse markSalaryAsPaid(SalaryRequest request);
    SalaryResponse updateSalary(SalaryRequest request);
    List<SalaryResponse> getAllSalaries(int page, int size);
    List<SalaryResponse> getSalariesByMonth(SalaryRequest request);
    SalaryResponse deleteSalaryByUserAndMonth(SalaryRequest request);
    SalarySummaryResponse getTotalSalaryPaid(YearMonth start, YearMonth end);
    List<MonthlySalaryResponse> getMonthlySalaryOverview(int year);
}
