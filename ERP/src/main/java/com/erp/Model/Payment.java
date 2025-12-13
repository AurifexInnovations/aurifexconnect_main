package com.erp.Model;

import com.erp.Enum.PaymentMethod;
import com.erp.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "customer_id")
    private Long customerId;



    @Column(name = "invoice_amount",  precision = 12, scale = 2)
    private BigDecimal invoiceAmount;

    @Column(name = "amount_paid", precision = 12, scale = 2)
    private BigDecimal amountPaid;

    @Column(
            name = "total_paid_till_now",
            precision = 12,
            scale = 2,
            columnDefinition = "numeric(12,2) default 0"
    )
    private BigDecimal totalPaidTillNow;

    @Column(
            name = "balance_amount",
            precision = 12,
            scale = 2,
            columnDefinition = "numeric(12,2) default 0"
    )
    private BigDecimal balanceAmount;



    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;



    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;



    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(
            name = "payment_date",
            columnDefinition = "timestamp default current_timestamp"
    )
    private LocalDateTime paymentDate;

    @Column(columnDefinition = "TEXT")
    private String notes;



    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
