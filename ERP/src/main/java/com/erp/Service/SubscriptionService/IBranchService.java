package com.erp.Service.SubscriptionService;

import com.erp.Dto.Request.BranchRqst;
import com.erp.Dto.Response.BranchRpns;
import com.erp.Dto.SubscriptionsDto.BranchDto;

public interface IBranchService {
    BranchDto fetchBranchByCode(String branchCode);
    BranchRpns createBranch(BranchRqst request);

}
