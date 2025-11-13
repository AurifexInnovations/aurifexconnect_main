package com.erp.Mapper.followup;


import com.erp.Dto.Request.FollowUpRequestDto;
import com.erp.Model.FollowUpDetails;
import org.springframework.stereotype.Component;

@Component
public class FollowUpMapper {

    public FollowUpDetails toEntity(FollowUpRequestDto dto) {
        return FollowUpDetails.builder()
                .id(dto.getId())
                .leadId(dto.getLeadId())
                .customerId(dto.getCustomerId())
                .followUpType(dto.getFollowUpType())
                .notes(dto.getNotes())
                .nextFollowUpDate(dto.getNextFollowUpDate())
                .nextFollowUpTime(dto.getNextFollowUpTime())
                .status(dto.getStatus())
                .completionDate(dto.getCompletionDate())
                .build();
    }

    public void updateEntity(FollowUpDetails entity, FollowUpRequestDto dto) {
        entity.setLeadId(dto.getLeadId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setFollowUpType(dto.getFollowUpType());
        entity.setNotes(dto.getNotes());
        entity.setNextFollowUpDate(dto.getNextFollowUpDate());
        entity.setNextFollowUpTime(dto.getNextFollowUpTime());
        entity.setStatus(dto.getStatus());
        entity.setCompletionDate(dto.getCompletionDate());
    }
}
