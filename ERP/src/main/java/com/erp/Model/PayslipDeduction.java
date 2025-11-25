package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "payslip_deductions")
public class PayslipDeduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deduction_id")
    private Long id;

    @Column(name = "payslip_id")
    private Long payslipId;

    @Column(name = "deduction_type")
    private String deductionType;

    @Column(name = "amount")
    private BigDecimal amount;
}
