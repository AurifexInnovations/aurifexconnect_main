package com.erp.Service.BranchService;

import com.erp.Dto.Request.BranchRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.DropDown;
import com.erp.Dto.Response.BranchResponse;
import com.erp.Dto.Response.ResultDto;

import java.util.List;

public interface BranchService
{
    BranchResponse createBranch(BranchRequest branchRequest);

    BranchResponse updateBranch(BranchRequest branchRequest);

    ResultDto<BranchResponse> getAllBranches();

//    PaginationResponse<BranchResponseId> getAllBranchesWithId(PaginationRequest request);

    BranchResponse deleteBranchById(CommanParam param);

    List<BranchResponse> getByIdOrBranchNameOrLocationOrBranchStatus(CommanParam param);

    List<BranchResponse> getBranchesByItemName(CommanParam param);

    ResultDto<BranchResponse> getBranchDetails(FilterRequest filterRequest);

    ResultDto<DropDown> getBranchDropDownList();
}
