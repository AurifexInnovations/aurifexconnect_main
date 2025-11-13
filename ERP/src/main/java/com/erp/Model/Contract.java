package com.erp.Model;

import com.erp.Enum.ContractStatus;
import com.erp.Enum.ServiceFrequency;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;

    @Column(name = "quotation_id", length = 50)
    private String quotationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_status", nullable = false)
    private ContractStatus contractStatus = ContractStatus.DRAFT;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_frequency", nullable = false)
    private ServiceFrequency serviceFrequency;

    @Column(name = "payment_terms", nullable = false, length = 100)
    private String paymentTerms;

    @Column(name = "is_recurring")
    private Boolean isRecurring = true;

    @Column(name = "activation_date")
    private LocalDate activationDate;

    @Column(name = "renewal_date")
    private LocalDate renewalDate;

    @Column(name = "contract_notes")
    private String contractNotes;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_modified_by", length = 100)
    private String lastModifiedBy;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt = LocalDateTime.now();
}
