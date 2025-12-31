package com.erp.Model;

import jakarta.persistence.*;
import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "amc_contract")
@Getter
@Setter
public class Amc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amc_id")
    private Long amcId;

    @Column(name = "amc_number")
    private String amcNumber;

    @Column(name = "amc_type")
    private String amcType;

    @Column(name = "amc_category")
    private String amcCategory;

    @Column(name = "recurring_type")
    private String recurringType;

    @Column(name = "recurring_interval")
    private Integer recurringInterval;

    @Column(name = "recurring_cycle")
    private Integer recurringCycle;

    @Column(name = "total_cycle")
    private Integer totalCycle;

    @Column(name = "complete_cycle")
    private Integer completeCycle;

    @Column(name = "remain_cycle")
    private Integer remainCycle;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    @Column(name = "next_invoice_date")
    private LocalDate nextInvoiceDate;

    @Column(name = "last_invoice_date")
    private LocalDate lastInvoiceDate;

    @Column(name = "contract_total_value")
    private BigDecimal contractTotalValue;

    @Column(name = "per_cycle_amount")
    private BigDecimal perCycleAmount;

    @Column(name = "total_complete_amount")
    private BigDecimal totalCompleteAmount;

    @Column(name = "total_remain_amount")
    private BigDecimal totalRemainAmount;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "amc_status")
    private String amcStatus;

    @Column(name = "pause_reason")
    private String pauseReason;

    @Column(name = "termination_reason")
    private String terminationReason;

    @Column(name = "auto_generate_invoice")
    private Boolean autoGenerateInvoice;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn( name = "customer_id",       // column in AMC table
            referencedColumnName = "id",    // PK in customer table
            nullable = false)
    private CustomerDetails customerDetails;

    @ManyToOne
    @JoinColumn( name = "sales_order_id",          // column in AMC table
            referencedColumnName = "sales_order_number",  // UNIQUE in sales_order
            nullable = false)
    private SalesOrder salesOrder;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

}
