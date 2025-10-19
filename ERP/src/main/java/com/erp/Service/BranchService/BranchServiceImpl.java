package com.erp.Service.BranchService;

import com.erp.Dto.PaginationResponse;
import com.erp.Dto.Request.BranchRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.PaginationRequest;
import com.erp.Dto.Response.BranchResponse;
import com.erp.Dto.Response.BranchResponseId;
import com.erp.Exception.Admin.AdminNotFoundException;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Mapper.Branch.BranchMapper;
import com.erp.Model.Admin;
import com.erp.Model.Branch;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Security.util.UserIdentity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BranchServiceImpl implements BranchService
{
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final InventoryRepository inventoryRepository;
    private final UserIdentity userIdentity;
    private final AdminUserRepository adminUserRepository;

    @Override
    public BranchResponse createBranch(BranchRequest branchRequest)
    {
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
    public BranchResponse updateBranch(BranchRequest branchRequest)
    {
        Long id = userIdentity.getCurrentUser().getId();

        Admin admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException(
                        "Admin not found with Id: " + branchRequest.getId()
                ));

        // Fetch existing entity
        Branch branch = branchRepository.findById(branchRequest.getId())
                .orElseThrow(() -> new BranchNotFoundException(
                        "Branch not found with Id: " + branchRequest.getId()
                ));

        // Update all fields except the ID
        branchMapper.mapToBranchEntityForUpdate(branchRequest, branch); // new method that skips setting ID
        branch.setEditedBy(admin.getName());

        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.mapToBranchResponse(updatedBranch);
    }


    @Override
    public BranchResponse deleteBranchById(CommanParam param)
    {
        Branch branch = branchRepository.findById(param.getId())
                .orElseThrow(()-> new BranchNotFoundException("Branch Not Found, Invalid Id "+param.getId()));
        branchRepository.deleteById(param.getId());
        return branchMapper.mapToBranchResponse(branch);
    }

    @Override
    public PaginationResponse<BranchResponse> getAllBranches(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
        Page<Branch> pageResult = branchRepository.findAll(pageable);

        PaginationResponse<BranchResponse> response = new PaginationResponse<>();
        response.setPageNumber(pageResult.getNumber());
        response.setPageSize(pageResult.getSize());
        response.setTotalRecords(pageResult.getTotalElements());
        response.setTotalPages(pageResult.getTotalPages());
        response.setData(branchMapper.mapToBranchResponse(pageResult.getContent()));

        return response;
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
        List<Branch> branches = branchRepository.findByBranchIdOrBranchNameOrLocationOrBranchStatus(param.getId(),param.getName(),param.getLocation(),param.getBranchStatus());
        if (branches.isEmpty()) {
            throw new BranchNotFoundException("No branches Found, Invalid Details Given ");
        }else {
            return branchMapper.mapToBranchResponse(branches);
        }
    }

    @Override
    public List<BranchResponse> getBranchesByItemName(CommanParam param){
        List<Branch> branches = branchRepository.findBranchByInventories_ItemName(param.getName());

        if(branches.isEmpty()){
            throw new InventoryNotFoundException("No Branches found Stocking Item: "+param.getName());
        }
        return branchMapper.mapToBranchResponse(branches);
    }
}