package com.erp.Service.SubscriptionService;

import com.erp.Dto.Request.BranchRqst;
import com.erp.Dto.Response.BranchRpns;
import com.erp.Dto.SubscriptionsDto.BranchDto;
import com.erp.Mapper.SubscriptionModule.BranchMapper;
import com.erp.Model.BranchEntity;
import com.erp.Repository.SubscriptionModule.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class BranchServiceImpl implements IBranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Override
    public BranchDto fetchBranchByCode(String branchCode) {
        try {
            BranchEntity entity = branchRepository.findByBranchCode(branchCode).orElse(null);
            return entity != null ? BranchMapper.toDto(entity) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BranchRpns createBranch(BranchRqst request) {
        BranchEntity entity = new BranchEntity();
        entity.setBranchNo(request.getBranchNo());
        entity.setBranchName(request.getBranchName());
        entity.setBranchCode(request.getBranchCode());
        entity.setLocation(request.getLocation());
        entity.setCompanyCode(request.getCompanyCode());
        entity.setCreatedBy(request.getCreatedBy());
        entity.setActiveYn("Y");
        entity.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));

        BranchEntity saved = branchRepository.save(entity);

        BranchRpns response = new BranchRpns();
        response.setBranchId(saved.getBranchId());
        response.setBranchName(saved.getBranchName());
        response.setBranchCode(saved.getBranchCode());
        response.setLocation(saved.getLocation());
        response.setActiveYn(saved.getActiveYn());

        return response;
    }

}