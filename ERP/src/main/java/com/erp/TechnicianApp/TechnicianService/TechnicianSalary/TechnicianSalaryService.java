package com.erp.TechnicianApp.TechnicianService.TechnicianSalary;

import com.erp.Dto.Request.Param;
import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.SalaryResponse;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface TechnicianSalaryService {

    SalaryResponse generateSalaryForMonth(SalaryRequest request);

    SalaryResponse getSalaryByUserAndMonth(SalaryRequest request);

    List<SalaryResponse> getSalariesByUserId(Param param);

    List<SalaryResponse> getSalariesByMonth(SalaryRequest request);

    List<SalaryResponse> getAllSalaries(int page, int size);

    SalaryResponse updateSalary(SalaryRequest request);

    SalaryResponse deleteSalaryByUserAndMonth(SalaryRequest request);

    SalaryResponse markSalaryAsPaid(SalaryRequest request);

    Map<String, Object> getSalaryOverview(int year, YearMonth startMonth, YearMonth endMonth);
}