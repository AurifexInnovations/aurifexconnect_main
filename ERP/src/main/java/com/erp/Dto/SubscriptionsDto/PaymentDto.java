package com.erp.Dto.SubscriptionsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long paymentId;
    private String branchCode;
    private Integer noOfBranch;
    private String branchPlan;
    private Integer noOfTechnicians;
    private String technicianPlan;
    private String paymentStatus;
    private String activeYn;
//    private String createdBy;
//    private Timestamp createdOn;
//    private String deletedBy;
//    private Timestamp deletedOn;
}