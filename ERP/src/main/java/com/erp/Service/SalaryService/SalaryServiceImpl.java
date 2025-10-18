package com.erp.Service.SalaryService;

import com.erp.CustomRepository.SalaryCustomRepository;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.MonthlySalaryResponse;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.Dto.Response.SalarySummaryResponse;
import com.erp.Enum.AmountStatus;
import com.erp.Exception.Salary.SalaryNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.Salary.SalaryMapper;
import com.erp.Model.Salary;
import com.erp.Model.User;
import com.erp.Repository.Attendance.AttendanceRepository;
import com.erp.Repository.Salary.SalaryRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Service.Attendance.AttendanceService;
import com.erp.Utility.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalaryServiceImpl implements SalaryService {

    private final AttendanceService attendanceService;
    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;
    private final SalaryMapper salaryMapper;
    private final AttendanceRepository attendanceRepository;

    private final SalaryCustomRepository salaryCustomRepository;

    @Override
    public SalaryResponse generateSalaryForMonth(SalaryRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        YearMonth month = request.getMonth();
        int workingDays = Optional.ofNullable(request.getWorkingDays()).orElse(month.lengthOfMonth());
        if (workingDays == 0) throw new IllegalArgumentException("Working days cannot be zero.");

        int paidDays = Optional.ofNullable(request.getPaidDays())
                .orElse(attendanceService.countPresentDaysByUserIdAndMonth(request.getUserId(), month));

        long baseSalary = Optional.ofNullable(request.getBaseSalary()).orElse(0L);
        long bonus = Optional.ofNullable(request.getBonus()).orElse(0L);
        long deductions = Optional.ofNullable(request.getDeductions()).orElse(0L);

        if (deductions == 0) {
            deductions = (baseSalary / workingDays) * (workingDays - paidDays);
        }

        long netSalary = calculateNetSalary(baseSalary, paidDays, workingDays, bonus, deductions);

        Salary salary = salaryRepository.findByUserIdAndMonth(request.getUserId(), month).orElse(null);

        if (salary != null && AmountStatus.PAID.equals(salary.getAmountStatus())) {
            return salaryMapper.mapToResponse(salary);
        }

        if (salary == null) {
            salary = new Salary();
        }

        salary = salaryMapper.mapToSalary(request, salary);
        salary.setUser(user);
        salary.setNetSalary(netSalary);
        salary.setAmountStatus(Optional.ofNullable(salary.getAmountStatus()).orElse(AmountStatus.PENDING));

        salaryRepository.save(salary);
        return salaryMapper.mapToResponse(salary);
    }

    @Override
    public SalaryResponse getSalaryByUserAndMonth(SalaryRequest request) {
        Salary salary = findByUserIdAndMonth(request.getUserId(), request.getMonth());
        return salaryMapper.mapToResponse(salary);
    }

    @Override
    public List<SalaryResponse> getSalariesByUserId(Param param) {
        List<Salary> salaries = salaryRepository.findByUserId(param.getUserId());
        if (salaries.isEmpty()) throw new SalaryNotFoundException("No salary records found for user.");
        return salaryMapper.mapToResponseList(salaries);
    }

    @Override
    public List<SalaryResponse> getSalariesByMonth(SalaryRequest request) {
        List<Salary> salaries = salaryRepository.findByMonth(request.getMonth());
        if (salaries.isEmpty()) throw new SalaryNotFoundException("No salary records found for the given month.");
        return salaryMapper.mapToResponseList(salaries);
    }

    @Override
    public List<SalaryResponse> getAllSalaries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("month").descending());
        List<Salary> salaries = salaryRepository.findAll(pageable).getContent();
        if (salaries.isEmpty()) throw new SalaryNotFoundException("No salary records found.");
        return salaryMapper.mapToResponseList(salaries);
    }

    @Override
    public SalaryResponse updateSalary(SalaryRequest request) {
        Salary salary = findByUserIdAndMonth(request.getUserId(), request.getMonth());

        int paidDays = Optional.ofNullable(request.getPaidDays()).orElse(salary.getPaidDays());
        long baseSalary = Optional.ofNullable(request.getBaseSalary()).orElse(salary.getBaseSalary());
        long bonus = Optional.ofNullable(request.getBonus()).orElse(salary.getBonus());
        long deductions = Optional.ofNullable(request.getDeductions()).orElse(salary.getDeductions());

        long netSalary = calculateNetSalary(baseSalary, paidDays, salary.getWorkingDays(), bonus, deductions);

        salary = salaryMapper.mapToSalary(request, salary);
        salary.setPaidDays(paidDays);
        salary.setNetSalary(netSalary);

        salaryRepository.save(salary);
        return salaryMapper.mapToResponse(salary);
    }

    @Override
    public SalaryResponse deleteSalaryByUserAndMonth(SalaryRequest request) {
        Salary salary = findByUserIdAndMonth(request.getUserId(), request.getMonth());
        salaryRepository.delete(salary);
        return salaryMapper.mapToResponse(salary);
    }

    @Override
    public SalaryResponse markSalaryAsPaid(SalaryRequest request) {
        Salary salary = findByUserIdAndMonth(request.getUserId(), request.getMonth());
        salary.setAmountStatus(AmountStatus.PAID);
        salary.setPaymentDate(YearMonth.from(LocalDate.now()));
        salaryRepository.save(salary);
        return salaryMapper.mapToResponse(salary);
    }

    // Helper methods
    private Salary findByUserIdAndMonth(Long userId, YearMonth month) {
        return salaryRepository.findByUserIdAndMonth(userId, month)
                .orElseThrow(() -> new SalaryNotFoundException("Salary not found for User ID: " + userId + ", Month: " + month));
    }

    private long calculateNetSalary(long base, int paid, int working, long bonus, long deductions) {
        long gross = (base * paid / working) + bonus;
        return Math.max(0L, gross - deductions);
    }

    @Override
    public Map<String, Object> getSalaryOverview(int year, YearMonth startMonth, YearMonth endMonth) {
        // Monthly Overview
        List<MonthlySalaryResponse> monthlyList = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            YearMonth ym = YearMonth.of(year, month);
            List<Salary> salaries = salaryRepository.findByPaymentDateAndAmountStatus(ym, AmountStatus.PAID);
            double total = salaries.stream().mapToDouble(Salary::getNetSalary).sum();
            monthlyList.add(new MonthlySalaryResponse(ym.getMonth().name(), total));
        }

        // Total Summary
        List<Salary> salariesInRange = salaryRepository.findByPaymentDateBetweenAndAmountStatus(
                startMonth, endMonth, AmountStatus.PAID
        );
        double totalAmount = salariesInRange.stream().mapToDouble(Salary::getNetSalary).sum();
        List<SalarySummaryResponse> summaryList = new ArrayList<>();
        SalarySummaryResponse summary = new SalarySummaryResponse();
        summary.setLabel("Total");
        summary.setAmount(totalAmount);
        summaryList.add(summary);

        // Response map
        Map<String, Object> result = new HashMap<>();
        result.put("monthlyOverview", monthlyList);
        result.put("totalSalaryPaid", summaryList);

        return result;
    }

    @Override
    public List<SalaryResponse> getSalaryDetails(FilterRequest filterRequest){
        log.info("Into [SalaryServiceImpl] [getSalaryDetails] ");

        log.info("[SalaryServiceImpl] [getSalaryDetails]  :: Request {} " , ObjectMapperUtils.writeValueAsString(filterRequest));

        List<SalaryResponse> salaryResponses = new ArrayList<>();

        try{
            salaryResponses = salaryCustomRepository.getSalaryDetails(filterRequest);
        }catch (Exception exception){
            log.error("Error [SalaryServiceImpl] [getSalaryDetails] ");
        }

        log.info("Exit [SalaryServiceImpl] [getSalaryDetails] ");

        return salaryResponses;
    }
}