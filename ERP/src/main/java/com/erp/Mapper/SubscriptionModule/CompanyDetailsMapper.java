package com.erp.Mapper.SubscriptionModule;

import com.erp.Dto.SubscriptionsDto.CompanyDetailsDto;
import com.erp.Model.CompanyDetailsEntity;

public class CompanyDetailsMapper {

    public static CompanyDetailsDto toDto(CompanyDetailsEntity entity) {
        CompanyDetailsDto dto = new CompanyDetailsDto();
        dto.setCompanyId(entity.getCompanyId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setCompanyCode(entity.getCompanyCode());
        dto.setActiveYn(entity.getActiveYn());
//        dto.setCreatedBy(entity.getCreatedBy());
//        dto.setCreatedOn(entity.getCreatedOn());
//        dto.setDeletedBy(entity.getDeletedBy());
//        dto.setDeletedOn(entity.getDeletedOn());
        return dto;
    }

    public static CompanyDetailsEntity toEntity(CompanyDetailsDto dto) {
        CompanyDetailsEntity entity = new CompanyDetailsEntity();
        entity.setCompanyId(dto.getCompanyId());
        entity.setCompanyName(dto.getCompanyName());
        entity.setCompanyCode(dto.getCompanyCode());
        entity.setActiveYn(dto.getActiveYn());
//        entity.setCreatedBy(dto.getCreatedBy());
//        entity.setCreatedOn(dto.getCreatedOn());
//        entity.setDeletedBy(dto.getDeletedBy());
//        entity.setDeletedOn(dto.getDeletedOn());
        return entity;
    }

}