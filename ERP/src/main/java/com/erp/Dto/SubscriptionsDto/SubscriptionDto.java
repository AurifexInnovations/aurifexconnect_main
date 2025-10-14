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
    private String subscriptionPlan;
    private String planPeriod;
    private LocalDate planStartDate;
    private LocalDate planEndDate;
    private String branchCode;
    private String companyCode;
    private String paymentStatus;
    private Long paymentId;
    private String activeYn;
//    private String createdBy;
//    private Timestamp createdOn;
//    private String updatedBy;
//    private Timestamp updatedOn;
//    private String deletedBy;
//    private Timestamp deletedOn;
}