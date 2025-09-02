package com.erp.TechnicianApp.TechnicianService.TechnicianSalary;

import com.erp.Dto.Request.Param;
import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.Service.Helper.SalaryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TechnicianSalaryServiceImpl implements TechnicianSalaryService {

    private final SalaryHelper salaryHelper;

    @Override
    public SalaryResponse generateSalaryForMonth(SalaryRequest request) {
        return salaryHelper.generateSalaryForMonth(request);
    }

    @Override
    public SalaryResponse getSalaryByUserAndMonth(SalaryRequest request) {
        return salaryHelper.getSalaryByUserAndMonth(request);
    }

    @Override
    public List<SalaryResponse> getSalariesByUserId(Param param) {
        return salaryHelper.getSalariesByUserId(param);
    }

    @Override
    public List<SalaryResponse> getSalariesByMonth(SalaryRequest request) {
        return salaryHelper.getSalariesByMonth(request);
    }

    @Override
    public List<SalaryResponse> getAllSalaries(int page, int size) {
        return salaryHelper.getAllSalaries(page, size);
    }

    @Override
    public SalaryResponse updateSalary(SalaryRequest request) {
        return salaryHelper.updateSalary(request);
    }

    @Override
    public SalaryResponse deleteSalaryByUserAndMonth(SalaryRequest request) {
        return salaryHelper.deleteSalaryByUserAndMonth(request);
    }

    @Override
    public SalaryResponse markSalaryAsPaid(SalaryRequest request) {
        return salaryHelper.markSalaryAsPaid(request);
    }

    @Override
    public Map<String, Object> getSalaryOverview(int year, YearMonth startMonth, YearMonth endMonth) {
        return salaryHelper.getSalaryOverview(year, startMonth, endMonth);
    }
}
