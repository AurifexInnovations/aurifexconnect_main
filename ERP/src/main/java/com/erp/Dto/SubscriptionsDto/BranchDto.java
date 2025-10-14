package com.erp.Dto.SubscriptionsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto {

    private Long branchId;
    private String branchNo;
    private String branchName;
    private String branchCode;
    private String location;
    private String companyCode;
    private String activeYn;
//    private String createdBy;
//    private Timestamp createdOn;
//    private String deletedBy;
//    private Timestamp deletedOn;
}