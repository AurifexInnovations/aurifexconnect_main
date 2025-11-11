package com.erp.Dto.SubscriptionsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {

    private Long subscriptionId;
    private String userId;
    private LocalDate planStartDate;
    private LocalDate planEndDate;
    private String totalBranches;   //---
    private String totalTechnicians;  //--
    private String totalAmount;  //--
    private String accountUser;
    private String branchCode;
    private String companyCode;
    private String paymentStatus;
    private String planPeriod;
    private String transactionPaymentId;
    private String activeYn;
    private String createdAt;
    private String updatedAt;
//    private String createdBy;
//    private Timestamp createdOn;
//    private String updatedBy;
//    private Timestamp updatedOn;
//    private String deletedBy;
//    private Timestamp deletedOn;
}