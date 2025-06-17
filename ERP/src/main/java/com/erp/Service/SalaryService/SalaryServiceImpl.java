package com.erp.Service.SalaryService;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {

    private final AttendanceService attendanceService;
    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;
    private final SalaryMapper salaryMapper;
    private final AttendanceRepository attendanceRepository;

    @Override
    public SalaryResponse generateSalaryForMonth(SalaryRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        YearMonth month = request.getMonth();
        int workingDays = Optional.ofNullable(request.getWorkingDays()).orElse(month.lengthOfMonth());
        int paidDays = Optional.ofNullable(request.getPaidDays())
                .orElse(attendanceService.countPresentDaysByUserIdAndMonth(request.getUserId(), month));
        long baseSalary = request.getBaseSalary();
        long bonus = request.getBonus();
        long deductions = request.getDeductions();
        if (deductions == 0) {
            deductions = (baseSalary / workingDays) * (workingDays - paidDays);
        }

        long netSalary = Math.max(0L, (baseSalary * paidDays / workingDays) - deductions + bonus);
        Salary salary = salaryRepository.findByUserIdAndMonth(request.getUserId(), month)
                .orElse(new Salary());
        if (salary.getAmountStatus() == AmountStatus.PAID) {
            return salaryMapper.mapToResponse(salary);
        }
        salary = salaryMapper.mapToSalary(request, salary);
        salary.setUser(user);
        salary.setNetSalary(netSalary);
        if (salary.getAmountStatus() == null) {
            salary.setAmountStatus(AmountStatus.PENDING);
        }
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
        List<Salary> salaries = salaryRepository.findAll();
        if (salaries.isEmpty()) throw new SalaryNotFoundException("No salary records found.");
        return salaryMapper.mapToResponseList(salaries);
    }

    @Override
    public SalaryResponse updateSalary(SalaryRequest request) {
        Salary salary = findByUserIdAndMonth(request.getUserId(), request.getMonth());
        int paidDays = Optional.ofNullable(request.getPaidDays()).orElse(salary.getPaidDays());
        long baseSalary = request.getBaseSalary();
        long bonus = request.getBonus();
        long deductions = request.getDeductions();
        long netSalary = Math.max(0L, (baseSalary * paidDays / salary.getWorkingDays()) - deductions + bonus);
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
        salary.setPaymentDate(YearMonth.now());
        salaryRepository.save(salary);
        return salaryMapper.mapToResponse(salary);
    }

    private Salary findByUserIdAndMonth(Long userId, YearMonth month) {
        return salaryRepository.findByUserIdAndMonth(userId, month)
                .orElseThrow(() -> new SalaryNotFoundException(
                        "Salary not found for User ID: " + userId + ", Month: " + month));
    }


    @Override
    public SalarySummaryResponse getTotalSalaryPaid(YearMonth startMonth, YearMonth endMonth) {
        List<Salary> salaries = salaryRepository.findByPaymentDateBetweenAndAmountStatus(
                startMonth, endMonth, AmountStatus.PAID);

        double totalAmount = salaries.stream()
                .mapToDouble(Salary::getNetSalary)
                .sum();

        SalarySummaryResponse response = new SalarySummaryResponse();
        response.setLabel("Total");
        response.setAmount(totalAmount);
        return response;
    }

    @Override
    public List<MonthlySalaryResponse> getMonthlySalaryOverview(int year) {
        List<MonthlySalaryResponse> monthlyList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            YearMonth ym = YearMonth.of(year, month);

            List<Salary> salaries = salaryRepository.findByPaymentDateAndAmountStatus(ym, AmountStatus.PAID);

            double total = salaries.stream()
                    .mapToDouble(Salary::getNetSalary)
                    .sum();

            monthlyList.add(new MonthlySalaryResponse(ym.getMonth().name(), total));
        }

        return monthlyList;
    }
}