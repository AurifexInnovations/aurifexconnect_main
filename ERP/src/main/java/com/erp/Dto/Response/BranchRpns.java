package com.erp.Dto.Response;

import lombok.Data;

@Data
public class BranchRpns {
    private Long branchId;
    private String branchName;
    private String branchCode;
    private String location;
    private String activeYn;
}
