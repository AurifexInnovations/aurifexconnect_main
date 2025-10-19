package com.erp.Mapper.SubscriptionModule;

import com.erp.Dto.SubscriptionsDto.PaymentDto;
import com.erp.Model.PaymentEntity;

public class PaymentMapper {

    public static PaymentDto toDto(PaymentEntity entity) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(entity.getPaymentId());
        dto.setBranchCode(entity.getBranchCode());
        dto.setNoOfBranch(entity.getNoOfBranch());
        dto.setBranchPlan(entity.getBranchPlan());
        dto.setNoOfTechnicians(entity.getNoOfTechnicians());
        dto.setTechnicianPlan(entity.getTechnicianPlan());
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setActiveYn(entity.getActiveYn());
//        dto.setCreatedBy(entity.getCreatedBy());
//        dto.setCreatedOn(entity.getCreatedOn());
//        dto.setDeletedBy(entity.getDeletedBy());
//        dto.setDeletedOn(entity.getDeletedOn());
        return dto;
    }

    public static PaymentEntity toEntity(PaymentDto dto) {
        PaymentEntity entity = new PaymentEntity();
        entity.setPaymentId(dto.getPaymentId());
        entity.setBranchCode(dto.getBranchCode());
        entity.setNoOfBranch(dto.getNoOfBranch());
        entity.setBranchPlan(dto.getBranchPlan());
        entity.setNoOfTechnicians(dto.getNoOfTechnicians());
        entity.setTechnicianPlan(dto.getTechnicianPlan());
        entity.setPaymentStatus(dto.getPaymentStatus());
        entity.setActiveYn(dto.getActiveYn());
//        entity.setCreatedBy(dto.getCreatedBy());
//        entity.setCreatedOn(dto.getCreatedOn());
//        entity.setDeletedBy(dto.getDeletedBy());
//        entity.setDeletedOn(dto.getDeletedOn());
        return entity;
    }

}