package com.erp.Dto.Request;

import lombok.Data;

@Data
public class BranchRqst {
    private String branchNo;
    private String branchName;
    private String branchCode;
    private String location;
    private String companyCode;
    private String createdBy;
}