package com.erp.Model;

import com.erp.Enum.AmountStatus;
import com.erp.Service.SalaryService.YearMonthAttributeConverter;
import jakarta.persistence.*;
import lombok.*;
import java.time.YearMonth;

@Entity
@Table(name = "salaries")
@Getter
@Setter
@EntityListeners(EntityListeners.class)
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    private User user;

    @Convert(converter = YearMonthAttributeConverter.class)
    @Column(name = "month")
    private YearMonth month;

    @Column(name = "base_salary")
    private long baseSalary;

    @Column(name = "working_days")
    private Integer workingDays;

    @Column(name = "paid_days")
    private Integer paidDays;

    @Column(name = "deductions")
    private long deductions;

    @Column(name = "bonus")
    private long bonus;

    @Column(name = "net_salary")
    private long netSalary;

    @Column(name = "remarks")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "amount_status")
    private AmountStatus amountStatus;

    @Convert(converter = YearMonthAttributeConverter.class)
    @Column(name = "payment_date")
    private YearMonth paymentDate;
}
