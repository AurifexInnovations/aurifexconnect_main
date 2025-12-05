package com.erp.Service.BranchService;

import com.erp.CustomRepository.BranchCustomRepository;
import com.erp.Dto.Request.BranchRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.DropDown;
import com.erp.Dto.Response.BranchResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.BranchStatus;
import com.erp.Exception.Admin.AdminNotFoundException;
import com.erp.Exception.Branch_Exception.BranchLimitExceededException;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.Branch.BranchMapper;
import com.erp.Meta.MetaAdminRepository;
import com.erp.Model.Admin;
import com.erp.Model.Branch;
import com.erp.Model.SubscriptionEntity;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.SubscriptionModule.SubscriptionRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Utility.ObjectMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final InventoryRepository inventoryRepository;
    private final UserIdentity userIdentity;
    private final AdminUserRepository adminUserRepository;

    private final BranchCustomRepository branchCustomRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MetaAdminRepository metaAdminRepository;

    @Override
    public BranchResponse createBranch(BranchRequest branchRequest) {
        SubscriptionEntity subscription = getSubscriptionDetails();

        long totalBranch = branchRepository.countByBranchStatus(BranchStatus.ACTIVE);

        if (totalBranch >= Integer.parseInt(subscription.getTotalBranches())) {
            throw new BranchLimitExceededException("You purchased only " + subscription.getTotalBranches() + " Branches, You have already " + subscription.getTotalBranches() + " ACTIVE Branches, Now You can not create more than this");
        }

        Long id = userIdentity.getCurrentUser().getId();

        Admin admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException(
                        "Admin not found with Id: " + branchRequest.getId()
                ));

        Branch branch = branchMapper.mapToBranch(branchRequest);
        branch.setEditedBy(admin.getName());

        branchRepository.save(branch);
        return branchMapper.mapToBranchResponse(branch);
    }

    @Override
    public BranchResponse updateBranch(BranchRequest branchRequest) {
        SubscriptionEntity subscription = getSubscriptionDetails();

        long totalBranches = branchRepository.countByBranchStatus(BranchStatus.ACTIVE);

        // Fetch existing entity
        Branch branch = branchRepository.findById(branchRequest.getId())
                .orElseThrow(() -> new BranchNotFoundException(
                        "Branch not found with Id: " + branchRequest.getId()
                ));

        if (branchRequest.getBranchStatus() == BranchStatus.ACTIVE &&
                totalBranches >= Integer.parseInt(subscription.getTotalBranches())) {
            throw new BranchLimitExceededException("You purchased only " + subscription.getTotalBranches() + " Branches, You have already " + subscription.getTotalBranches() + " ACTIVE Branches, Now You can not updated branch as ACTIVE");
        }

        Long id = userIdentity.getCurrentUser().getId();

        Admin admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException(
                        "Admin not found with Id: " + branchRequest.getId()
                ));

        // Update all fields except the ID
        branchMapper.mapToBranchEntityForUpdate(branchRequest, branch); // new method that skips setting ID
        branch.setEditedBy(admin.getName());

        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.mapToBranchResponse(updatedBranch);
    }


    @Override
    public BranchResponse deleteBranchById(CommanParam param) {
        Branch branch = branchRepository.findById(param.getId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found, Invalid Id " + param.getId()));
        branchRepository.deleteById(param.getId());
        return branchMapper.mapToBranchResponse(branch);
    }

    @Override
    public ResultDto<BranchResponse> getAllBranches() {

        ResultDto<BranchResponse> resultDto = new ResultDto<>();

        List<BranchResponse> list = branchMapper.mapListToBranchResponse(branchRepository.findAll());

        resultDto.setResults(list != null ? list : List.of());
        resultDto.setCount(list != null ? list.size() : 0);

        return resultDto;
    }

//    @Override
//    public PaginationResponse<BranchResponseId> getAllBranchesWithId(PaginationRequest request)
//    {
//        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
//        Page<Branch> pageResult = branchRepository.findAll(pageable);
//
//        PaginationResponse<BranchResponseId> response = new PaginationResponse<>();
//        response.setPageNumber(pageResult.getNumber());
//        response.setPageSize(pageResult.getSize());
//        response.setTotalRecords(pageResult.getTotalElements());
//        response.setTotalPages(pageResult.getTotalPages());
//        response.setData(branchMapper.mapToBranchResponseId(pageResult.getContent()));
//
//        return response;
//    }

    @Override
    public List<BranchResponse> getByIdOrBranchNameOrLocationOrBranchStatus(CommanParam param) {
        List<Branch> branches = branchRepository.findByBranchIdOrBranchNameOrLocationOrBranchStatus(param.getId(), param.getName(), param.getLocation(), param.getBranchStatus());
        if (branches.isEmpty()) {
            throw new BranchNotFoundException("No branches Found, Invalid Details Given ");
        } else {
            return branchMapper.mapToBranchResponse(branches);
        }
    }

    @Override
    public List<BranchResponse> getBranchesByItemName(CommanParam param) {
        List<Branch> branches = branchRepository.findBranchByInventories_ItemName(param.getName());

        if (branches.isEmpty()) {
            throw new InventoryNotFoundException("No Branches found Stocking Item: " + param.getName());
        }
        return branchMapper.mapToBranchResponse(branches);
    }

    public ResultDto<BranchResponse> getBranchDetails(FilterRequest filterRequest) {
        log.info("Into [BranchServiceImpl] [getBranchDetails] ");

        log.info("[BranchServiceImpl] [getBranchDetails] :: Request {} ",
                ObjectMapperUtils.writeValueAsString(filterRequest));

        ResultDto<BranchResponse> branchResponses = new ResultDto<>();

        try {
            branchResponses = branchCustomRepository.getBranchDetails(filterRequest);
        } catch (Exception exception) {
            log.error("Error [BranchServiceImpl] [getBranchDetails] :: {} :: {} ", exception.getMessage(), exception);
        }

        log.info("Exit [BranchServiceImpl] [getBranchDetails] ");

        return branchResponses;
    }

    private SubscriptionEntity getSubscriptionDetails() {
        String schemaName = TenantContext.getCurrentTenant();

        String email = metaAdminRepository.findAdminEmailBySchemaName(schemaName)
                .orElseThrow(() -> new AdminNotFoundException("Schema Not Found With : " + schemaName));

        SubscriptionEntity subscription = subscriptionRepository.findByUserId(email)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription Not Found for Email : " + email));

        return subscription;
    }

    @Override
    public ResultDto<DropDown> getBranchDropDownList() {
        List<Branch> branches = branchRepository.findAll();
        List<DropDown> dropdown = new ArrayList<>();

        for (Branch branch : branches) {
            if(branch.getBranchStatus() == BranchStatus.ACTIVE)
                dropdown.add(new DropDown(branch.getBranchId(), branch.getBranchName()));
        }
        ResultDto<DropDown> result = new ResultDto<>();
        result.setCount(dropdown.size());
        result.setResults(dropdown);
        return result;
    }
}