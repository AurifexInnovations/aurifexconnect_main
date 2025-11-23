package com.erp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "bill_id", nullable = false)
    private Long billId;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "date_paid", nullable = false)
    private LocalDate datePaid;

    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;

    @Column(name = "voucher_id", nullable = false)
    private Long voucherId;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "payment_number")
    private String paymentNumber;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();


    @Column(name = "updated_date")
    private LocalDate updatedDate = LocalDate.now();
}
