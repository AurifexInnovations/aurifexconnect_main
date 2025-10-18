package com.erp.Service.StaffService;

import com.erp.CustomRepository.StaffCustomRepository;
import com.erp.Dto.Request.*;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.StaffResponse;
import com.erp.Exception.Staff_Exception.StaffNotFoundException;
import com.erp.Mapper.Staff.StaffMapper;
import com.erp.Model.Branch;
import com.erp.Model.Staff;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Staff.StaffRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final BranchRepository branchRepository;

    private final StaffCustomRepository staffCustomRepository;


    @Override
    public StaffResponse createStaff(StaffRequest staffRequest) {
        Branch branch = branchRepository.findById(staffRequest.getBranchId())
                .orElseThrow(() -> new StaffNotFoundException("Branch not found for id " + staffRequest.getBranchId()));

        Staff staff = staffMapper.mapToStaff(staffRequest);
        staff.setBranch(branch);
        staffRepository.save(staff);
        return staffMapper.mapToStaffResponse(staff);
    }

    @Override
    public StaffResponse updateStaff(StaffRequest staffRequest) {
        Staff staff = staffRepository.findById(staffRequest.getId())
                .orElseThrow(() -> new StaffNotFoundException("Staff not found for id " + staffRequest.getId()));

        staffMapper.mapToStaffEntity(staffRequest, staff);
        staffRepository.save(staff);
        return staffMapper.mapToStaffResponse(staff);
    }

    @Override
    public StaffResponse deleteStaffById(CommanParam param) {
        Staff staff = staffRepository.findById(param.getId())
                .orElseThrow(() -> new StaffNotFoundException("Staff not found for id " + param.getId()));

        staffRepository.deleteById(param.getId());
        return staffMapper.mapToStaffResponse(staff);
    }

    @Override
    public List<StaffResponse> getStaffByIdOrNameOrDesignationOrStatus(StaffParam param) {
        List<Staff> staffList = staffRepository.findByIdOrStaffNameOrDesignationOrStaffStatus(
                param.getId(), param.getName(), param.getDesignation(), param.getStaffStatus()
        );

        if (staffList.isEmpty()) {
            throw new StaffNotFoundException("No staff found for given details.");
        }

        return staffMapper.mapToStaffResponse(staffList);
    }

    @Override
    public List<StaffResponse> getAllStaff(PaginationRequest request) {
        List<Staff> staffList;

        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
            Page<Staff> pageResult = staffRepository.findAll(pageable);
            staffList = pageResult.getContent();
        } else {
            staffList = staffRepository.findAll();
        }

        return staffMapper.mapToStaffResponse(staffList);
    }

    @Override
    public List<StaffResponse> getStaffByBranchId(CommanParam param) {
        List<Staff> staffList = staffRepository.findByBranch_BranchId(param.getId());

        if (staffList.isEmpty()) {
            throw new StaffNotFoundException("No staff found for branch id " + param.getId());
        }

        return staffMapper.mapToStaffResponse(staffList);
    }

    @Override
    public ResultDto<StaffResponse> getStaffDetails(FilterRequest filterRequest){
        log.info("Into [StaffServiceImpl] [getStaffDetails]");

        ResultDto<StaffResponse> staffResponses = new ResultDto<>();

        try{
            staffResponses = staffCustomRepository.getFilteredStaff(filterRequest);
        }catch (Exception exception){
            log.error("Error [StaffServiceImpl] [getStaffDetails] :: {} :: {}" , exception.getMessage() , exception);
        }
        log.info("Exit [StaffServiceImpl] [getStaffDetails]");

        return staffResponses;
    }

}
