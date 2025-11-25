package com.erp.Repository.PaySlip;

import com.erp.Model.PayslipDeduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayslipDeductionRepository extends JpaRepository<PayslipDeduction, Long> {

    List<PayslipDeduction> findByPayslipId(Long payslipId);
}
