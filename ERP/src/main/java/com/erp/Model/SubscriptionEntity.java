package com.erp.Model;

import com.erp.Enum.PaymentMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscription", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscriptionid")
    private Long subscriptionId;

    @Column(name = "userid", nullable = false)
    private String userId;

    @Column(name = "planstartdate")
    private LocalDate planStartDate;

    @Column(name = "total_branches")
    private String totalBranches;

    @Column(name = "total_technicians")
    private String totalTechnicians;

    @Column(name = "total_amount")
    private String totalAmount;

    @Column(name = "accountuser")
    private String accountUser;

    @Column(name = "planenddate")
    private LocalDate planEndDate;

    @Column(name = "planperiod")
    private String planPeriod;

    @Column(name = "branchcode", length = 50)
    private String branchCode;

    @Column(name = "companycode", length = 50)
    private String companyCode;

    @Column(name = "paymentstatus", length = 20)
    private String paymentStatus;

    @Column(name = "paymentid")
    private String paymentTransactionId;

    @Column(name = "activeYn", length = 1)
    private String activeYn;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

//    @ManyToOne
//    @JoinColumn(name = "branchcode", referencedColumnName = "branchcode", insertable = false, updatable = false)
//    private BranchEntity branch;
//
//    @ManyToOne
//    @JoinColumn(name = "companycode", referencedColumnName = "companycode", insertable = false, updatable = false)
//    private CompanyDetailsEntity company;
//
//    @ManyToOne
//    @JoinColumn(name = "paymentid", referencedColumnName = "paymentid", insertable = false, updatable = false)
//    private PaymentEntity payment;
}