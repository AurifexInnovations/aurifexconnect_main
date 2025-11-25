package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "payslips")
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payslip_id")
    private Long payslipId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "pay_period_start")
    private LocalDate payPeriodStart;

    @Column(name = "pay_period_end")
    private LocalDate payPeriodEnd;

    @Column(name = "generation_date")
    private LocalDate generationDate;

    @Column(name = "gross_salary")
    private BigDecimal grossSalary;

    @Column(name = "total_deductions")
    private BigDecimal totalDeductions;

    @Column(name = "net_pay")
    private BigDecimal netPay;

    @Column(name = "status")
    private String status;

    @Column(name = "payslip_number")
    private String payslipNumber;

    @Column(name = "voucher_id")
    private Long voucherId;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
