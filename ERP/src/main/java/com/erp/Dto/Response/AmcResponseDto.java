package com.erp.Dto.Response;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AmcResponseDto {


    // ===== Primary Info =====
    private Long amcId;
    private String amcNumber;
    private String amcType;
    private String amcCategory;
    private String amcStatus;

    // ===== Recurring Configuration =====
    private String recurringType;
    private Integer recurringInterval;
    private Integer recurringCycle;
    private Integer totalCycle;
    private Integer completeCycle;
    private Integer remainCycle;

    // ===== Contract Dates =====
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private LocalDate nextInvoiceDate;
    private LocalDate lastInvoiceDate;

    // ===== Amounts =====
    private BigDecimal contractTotalValue;
    private BigDecimal perCycleAmount;
    private BigDecimal totalCompleteAmount;
    private BigDecimal totalRemainAmount;
    private BigDecimal discountAmount;

    // ===== Automation Flags =====
    private Boolean autoGenerateInvoice;

    // ===== Status Reasons =====
    private String pauseReason;
    private String terminationReason;

    // ===== Relations (IDs + display fields only) =====
    private Long customerId;
   // private String customerName;

    private Long salesOrderId;

    private Long branchId;
   // private String branchName;

    // ===== Audit =====
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
