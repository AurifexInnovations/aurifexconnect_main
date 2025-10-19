package com.erp.Service.BranchService;

import com.erp.Dto.PaginationResponse;
import com.erp.Dto.Request.BranchRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.PaginationRequest;
import com.erp.Dto.Response.BranchResponse;
import com.erp.Dto.Response.BranchResponseId;

import java.util.List;

public interface BranchService
{
    BranchResponse createBranch(BranchRequest branchRequest);

    BranchResponse updateBranch(BranchRequest branchRequest);

    PaginationResponse<BranchResponse> getAllBranches(PaginationRequest request);

//    PaginationResponse<BranchResponseId> getAllBranchesWithId(PaginationRequest request);

    BranchResponse deleteBranchById(CommanParam param);

    List<BranchResponse> getByIdOrBranchNameOrLocationOrBranchStatus(CommanParam param);

    List<BranchResponse> getBranchesByItemName(CommanParam param);

}
