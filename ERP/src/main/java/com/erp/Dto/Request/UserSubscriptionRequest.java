package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSubscriptionRequest
{
    private String userId; // it can be anything like email or username
    private String companyCode; //its compulsory
    private String branchCode; //its optional
    private Integer totalBranches;   //---
    private Integer totalTechnicians;  //--
    private Integer totalAmount;  //--
    private Integer accountUser;  //--
    private String paymentStatus; //--
    private String paymentMethod; //--
    private String paymentTransactionId; //--
    private String planPeriod; //--
    private String planPeriodForBranches; // Free, Basic, Premium
    private String planPeriodForTechnicians; // Free, Basic, Premium
}
