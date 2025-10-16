package com.erp.Service.BranchService;

import com.erp.Dto.Request.BranchRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.PaginationRequest;
import com.erp.Dto.Response.BranchResponse;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Mapper.Branch.BranchMapper;
import com.erp.Model.Branch;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BranchServiceImpl implements BranchService{

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final InventoryRepository inventoryRepository;

    @Override
    public BranchResponse createBranch(BranchRequest branchRequest){
        Branch branch = branchMapper.mapToBranch(branchRequest);
        branchRepository.save(branch);
        return branchMapper.mapToBranchResponse(branch);
    }

    @Override
    public BranchResponse updateBranch(BranchRequest branchRequest){
        Branch branch = branchRepository.findById(branchRequest.getId())
                .orElseThrow(()-> new BranchNotFoundException("Branch not found with Id: " + branchRequest.getId()));

        branchMapper.mapToBranchEntity(branchRequest,branch);
        branchRepository.save(branch);
        return branchMapper.mapToBranchResponse(branch);
    }

    @Override
    public BranchResponse deleteBranchById(CommanParam param){
        Branch branch = branchRepository.findById(param.getId())
                .orElseThrow(()-> new BranchNotFoundException("Branch Not Found, Invalid Id "+param.getId()));
        branchRepository.deleteById(param.getId());
        return branchMapper.mapToBranchResponse(branch);
    }

    @Override
    public List<BranchResponse> getAllBranches(PaginationRequest request) {
        List<Branch> branches;

        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
            Page<Branch> pageResult = branchRepository.findAll(pageable);
            branches = pageResult.getContent();
        } else {
            // No pagination params — fetch all
            branches = branchRepository.findAll();
        }

        return branchMapper.mapToBranchResponse(branches);
    }

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