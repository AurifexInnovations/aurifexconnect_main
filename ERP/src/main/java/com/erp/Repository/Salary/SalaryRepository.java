package com.erp.Repository.Salary;

import com.erp.Enum.AmountStatus;
import com.erp.Model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    Optional<Salary> findByUserIdAndMonth(Long userId, YearMonth month);
    List<Salary> findByUserId(Long userId);
    List<Salary> findByMonth(YearMonth month);
    List<Salary> findByPaymentDateBetweenAndAmountStatus(YearMonth start, YearMonth end, AmountStatus status);
    List<Salary> findByPaymentDateAndAmountStatus(YearMonth paymentDate, AmountStatus status);
}
