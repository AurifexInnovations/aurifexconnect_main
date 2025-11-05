package com.erp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "subscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscriptionid")
    private Long subscriptionId;

    @Column(name = "userid", nullable = false)
    private String userId;

    @Column(name = "subscriptionplan", nullable = false, length = 100)
    private String subscriptionPlan;

    @Column(name = "planperiod", length = 50)
    private String planPeriod;

    @Column(name = "planstartdate")
    private LocalDate planStartDate;

    @Column(name = "accountuser")
    private String accountUser;

    @Column(name = "planenddate")
    private LocalDate planEndDate;

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