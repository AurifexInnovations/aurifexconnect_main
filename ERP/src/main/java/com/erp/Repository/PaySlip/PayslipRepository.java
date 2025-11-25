package com.erp.Repository.PaySlip;

import com.erp.Model.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayslipRepository extends JpaRepository<Payslip, Long> {

    Optional<Payslip> findByPayslipIdAndIsActiveTrue(Long id);
}
