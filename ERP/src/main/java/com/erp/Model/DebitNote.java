package com.erp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "debit_notes")
@Data
@NoArgsConstructor
public class DebitNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dn_id")
    private Long dnId;

    @Column(name = "bill_id", nullable = false)
    private Long billId;

    @Column(name = "date_issued", nullable = false)
    private LocalDate dateIssued;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "amount_debited", nullable = false)
    private BigDecimal amountDebited;

    @Column(name = "dn_number")
    private String dnNumber;

    @Column(name = "inventory_adjustment")
    private Boolean inventoryAdjustment;

    @Column(name = "tax_adjustment_amount")
    private BigDecimal taxAdjustmentAmount;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "status")
    private String status = "Applied";

    @Column(name = "is_active")
    private Boolean isActive = true;
}
