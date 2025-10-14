package com.erp.Mapper.SubscriptionModule;

import com.erp.Dto.SubscriptionsDto.SubscriptionDto;
import com.erp.Model.SubscriptionEntity;

public class SubscriptionMapper {

    public static SubscriptionDto toDto(SubscriptionEntity entity) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setSubscriptionId(entity.getSubscriptionId());
        dto.setUserId(entity.getUserId());
        dto.setSubscriptionPlan(entity.getSubscriptionPlan());
        dto.setPlanPeriod(entity.getPlanPeriod());
        dto.setPlanStartDate(entity.getPlanStartDate());
        dto.setPlanEndDate(entity.getPlanEndDate());
        dto.setBranchCode(entity.getBranchCode());
        dto.setCompanyCode(entity.getCompanyCode());
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setPaymentId(entity.getPaymentId());
        dto.setActiveYn(entity.getActiveYn());
//        dto.setCreatedBy(entity.getCreatedBy());
//        dto.setCreatedOn(entity.getCreatedOn());
//        dto.setUpdatedBy(entity.getUpdatedBy());
//        dto.setUpdatedOn(entity.getUpdatedOn());
//        dto.setDeletedBy(entity.getDeletedBy());
//        dto.setDeletedOn(entity.getDeletedOn());
        return dto;
    }

    public static SubscriptionEntity toEntity(SubscriptionDto dto) {
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setSubscriptionId(dto.getSubscriptionId());
        entity.setUserId(dto.getUserId());
        entity.setSubscriptionPlan(dto.getSubscriptionPlan());
        entity.setPlanPeriod(dto.getPlanPeriod());
        entity.setPlanStartDate(dto.getPlanStartDate());
        entity.setPlanEndDate(dto.getPlanEndDate());
        entity.setBranchCode(dto.getBranchCode());
        entity.setCompanyCode(dto.getCompanyCode());
        entity.setPaymentStatus(dto.getPaymentStatus());
        entity.setPaymentId(dto.getPaymentId());
        entity.setActiveYn(dto.getActiveYn());
//        entity.setCreatedBy(dto.getCreatedBy());
//        entity.setCreatedOn(dto.getCreatedOn());
//        entity.setUpdatedBy(dto.getUpdatedBy());
//        entity.setUpdatedOn(dto.getUpdatedOn());
//        entity.setDeletedBy(dto.getDeletedBy());
//        entity.setDeletedOn(dto.getDeletedOn());
        return entity;
    }

}