package com.erp.Model;

import com.erp.Enum.ServiceCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "enhance_quotation")
public class EnhanceQuotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @Column(name = "company_name", length = 150)
    private String companyName;

    @Column(length = 150)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "alternate_phone", length = 20)
    private String alternatePhone;

    @Column(name = "address_line_1", columnDefinition = "TEXT")
    private String addressLine1;

    @Column(name = "address_line_2", columnDefinition = "TEXT")
    private String addressLine2;

    @Column(columnDefinition = "TEXT")
    private String landmark;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 10)
    private String pincode;

    @Column(name = "location_url", columnDefinition = "TEXT")
    private String locationUrl;

    @Column(name = "quotation_number", length = 50, unique = true)
    private String quotationNumber;

//    @CreationTimestamp
//    @Column(name = "quotation_date")
//    private LocalDate quotationDate;

    @Column(name = "service_type", length = 20)
    private String serviceType;

    @Column(precision = 10, scale = 2)
    private BigDecimal sqft;

    @Column(precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "grand_total", precision = 12, scale = 2,
            insertable = false, updatable = false)
    private BigDecimal grandTotal;

    @Column(name = "quotation_type", length = 20, nullable = false)
    private String quotationType;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;

    @Column(name = "sent_via", length = 30)
    private String sentVia;

    @Column(name = "is_recurring")
    private Boolean isRecurring = false;

    @Column(name = "recurring_type", length = 20)
    private String recurringType;

    @Column(name = "recurring_interval")
    private Integer recurringInterval;

    @Column(name = "recurring_cycles")
    private Integer recurringCycles;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "next_recurring_date")
    private LocalDate nextRecurringDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

}
