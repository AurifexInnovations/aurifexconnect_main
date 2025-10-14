package com.erp.Mapper.SubscriptionModule;

import com.erp.Dto.SubscriptionsDto.TechnicianDto;
import com.erp.Model.TechnicianEntity;

public class TechnicianMapper {

    public static TechnicianDto toDto(TechnicianEntity entity) {
        TechnicianDto dto = new TechnicianDto();
        dto.setTechnicianId(entity.getTechnicianId());
        dto.setTechnicianName(entity.getTechnicianName());
        dto.setContact(entity.getContact());
        dto.setAge(entity.getAge());
        dto.setGender(entity.getGender());
        dto.setBranchCode(entity.getBranchCode());
        dto.setActiveYn(entity.getActiveYn());
//        dto.setCreatedBy(entity.getCreatedBy());
//        dto.setCreatedOn(entity.getCreatedOn());
//        dto.setDeletedBy(entity.getDeletedBy());
//        dto.setDeletedOn(entity.getDeletedOn());
        return dto;
    }

    public static TechnicianEntity toEntity(TechnicianDto dto) {
        TechnicianEntity entity = new TechnicianEntity();
        entity.setTechnicianId(dto.getTechnicianId());
        entity.setTechnicianName(dto.getTechnicianName());
        entity.setContact(dto.getContact());
        entity.setAge(dto.getAge());
        entity.setGender(dto.getGender());
        entity.setBranchCode(dto.getBranchCode());
        entity.setActiveYn(dto.getActiveYn());
//        entity.setCreatedBy(dto.getCreatedBy());
//        entity.setCreatedOn(dto.getCreatedOn());
//        entity.setDeletedBy(dto.getDeletedBy());
//        entity.setDeletedOn(dto.getDeletedOn());
        return entity;
    }

}