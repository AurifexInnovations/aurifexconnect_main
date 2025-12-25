package com.erp.Model;

import com.erp.Enum.InvoiceStatus;
import com.erp.Enum.InvoiceType;
import com.erp.Enum.ServiceCategory;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "sales_order_id")
    private Long salesOrderId;

//    @Column(name = "quotation_id")
//    private Long quotationId;

    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

//    @Column(name = "invoice_date")
//    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_category", length = 20)
    private ServiceCategory serviceCategory;

    @Column(name = "sqft", precision = 10, scale = 2)
    private BigDecimal sqft;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_is_for")
    private InvoiceType invoiceIsFor;


    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(
            name = "grand_total",
            precision = 12,
            scale = 2
    )
    private BigDecimal grandTotal;

    @Column(name = "amount_paid", precision = 12, scale = 2)
    private BigDecimal amountPaid = BigDecimal.ZERO;

    @Column(name = "balance_amount", precision = 12, scale = 2)
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private InvoiceStatus status;

    @Column(name = "payment_status")
    private String PaymentStatus = "UNPAID";

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

}
