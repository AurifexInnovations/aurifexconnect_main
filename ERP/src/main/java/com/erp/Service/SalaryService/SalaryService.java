package com.erp.Service.SalaryService;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.SalaryResponse;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface SalaryService {
    SalaryResponse generateSalaryForMonth(SalaryRequest request);
    List<SalaryResponse> getSalariesByUserId(Param param);
    SalaryResponse getSalaryByUserAndMonth(SalaryRequest request);
    SalaryResponse markSalaryAsPaid(SalaryRequest request);
    SalaryResponse updateSalary(SalaryRequest request);
    List<SalaryResponse> getAllSalaries(int page, int size);
    List<SalaryResponse> getSalariesByMonth(SalaryRequest request);
    SalaryResponse deleteSalaryByUserAndMonth(SalaryRequest request);
    Map<String, Object> getSalaryOverview(int year, YearMonth startMonth, YearMonth endMonth);
    ResultDto<SalaryResponse> getSalaryDetails(FilterRequest filterRequest);
    List<SalaryResponse> getAllSalaryData();

}