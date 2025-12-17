package com.erp.Model;

import com.erp.Enum.ServiceCategory;
import jakarta.persistence.*;
import lombok.*;
import org.apache.xpath.operations.Quo;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Leads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lead_name", nullable = false)
    private String leadName;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "source")
    private String source;

    @Column(name = "type_of_lead")
    private String typeOfLead;

    @Column(name = "lead_status")
    private String leadStatus;

    @Column(name = "engagement_score")
    private Integer engagementScore;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Alter mappings

    @Column(name = "lost_reason")
    private String lostReason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "converted_customer_id", unique = true)
    private CustomerDetails convertedCustomer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "latest_quotation_id")
    private Quotation latestQuotation;

    @Column(name = "total_quotation")
    private Long totalQuotation;

    @Column(name = "service_category")
    private ServiceCategory serviceCategory;   // Should be RESIDENTIAL / COMMERCIAL

    @Column(name = "sqrt")
    private Double sqrt;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}
