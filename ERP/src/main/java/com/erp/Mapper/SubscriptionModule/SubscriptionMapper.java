package com.erp.Mapper.SubscriptionModule;

import com.erp.Dto.SubscriptionsDto.SubscriptionDto;
import com.erp.Model.SubscriptionEntity;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionMapper {

    public static SubscriptionDto toDto(SubscriptionEntity entity) {
        SubscriptionDto dto = new SubscriptionDto();

        dto.setSubscriptionId(entity.getSubscriptionId());
        dto.setUserId(entity.getUserId());
        dto.setAccountUser(entity.getAccountUser());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setTotalTechnicians(entity.getTotalTechnicians());
        dto.setTotalBranches(entity.getTotalBranches());
        dto.setPlanPeriod(entity.getPlanPeriod());
        dto.setPlanStartDate(entity.getPlanStartDate());
        dto.setPlanEndDate(entity.getPlanEndDate());
        dto.setBranchCode(entity.getBranchCode());
        dto.setCompanyCode(entity.getCompanyCode());
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setTransactionPaymentId(entity.getPaymentTransactionId());
        dto.setActiveYn(entity.getActiveYn());
        dto.setCreatedAt(entity.getCreatedAt().toString());
        dto.setUpdatedAt(entity.getUpdatedAt().toString());

        return dto;
    }

    public static SubscriptionEntity toEntity(SubscriptionDto dto) {
        SubscriptionEntity entity = new SubscriptionEntity();

        entity.setSubscriptionId(dto.getSubscriptionId());
        entity.setUserId(dto.getUserId());
        entity.setAccountUser(dto.getAccountUser());
        entity.setTotalAmount(entity.getTotalAmount());
        entity.setTotalTechnicians(entity.getTotalTechnicians());
        entity.setTotalBranches(entity.getTotalBranches());
        entity.setPlanPeriod(dto.getPlanPeriod());
        entity.setPlanStartDate(dto.getPlanStartDate());
        entity.setPlanEndDate(dto.getPlanEndDate());
        entity.setBranchCode(dto.getBranchCode());
        entity.setCompanyCode(dto.getCompanyCode());
        entity.setPaymentStatus(dto.getPaymentStatus());
        entity.setPaymentTransactionId(dto.getTransactionPaymentId());
        entity.setActiveYn(dto.getActiveYn());
        dto.setCreatedAt(entity.getCreatedAt().toString());
        dto.setUpdatedAt(entity.getUpdatedAt().toString());

        return entity;
    }

    public static List<SubscriptionDto> toSubscriptionDtoList(List<SubscriptionEntity> entities){
        List<SubscriptionDto> list = new ArrayList<>();
        for(SubscriptionEntity subscription : entities){
            list.add(toDto(subscription));
        }
        return list;
    }
}