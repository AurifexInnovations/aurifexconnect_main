package com.erp.Mapper.SubscriptionModule;

import com.erp.Dto.SubscriptionsDto.BranchDto;
import com.erp.Model.BranchEntity;

public class BranchMapper {

    public static BranchDto toDto(BranchEntity entity) {
        BranchDto dto = new BranchDto();
        dto.setBranchId(entity.getBranchId());
        dto.setBranchNo(entity.getBranchNo());
        dto.setBranchName(entity.getBranchName());
        dto.setBranchCode(entity.getBranchCode());
        dto.setLocation(entity.getLocation());
        dto.setCompanyCode(entity.getCompanyCode());
        dto.setActiveYn(entity.getActiveYn());
//        dto.setCreatedBy(entity.getCreatedBy());
//        dto.setCreatedOn(entity.getCreatedOn());
//        dto.setDeletedBy(entity.getDeletedBy());
//        dto.setDeletedOn(entity.getDeletedOn());
        return dto;
    }


    public static BranchEntity toEntity(BranchDto dto) {
        BranchEntity entity = new BranchEntity();
        entity.setBranchId(dto.getBranchId());
        entity.setBranchNo(dto.getBranchNo());
        entity.setBranchName(dto.getBranchName());
        entity.setBranchCode(dto.getBranchCode());
        entity.setLocation(dto.getLocation());
        entity.setCompanyCode(dto.getCompanyCode());
        entity.setActiveYn(dto.getActiveYn());
//        entity.setCreatedBy(dto.getCreatedBy());
//        entity.setCreatedOn(dto.getCreatedOn());
//        entity.setDeletedBy(dto.getDeletedBy());
//        entity.setDeletedOn(dto.getDeletedOn());
        return entity;
    }

}