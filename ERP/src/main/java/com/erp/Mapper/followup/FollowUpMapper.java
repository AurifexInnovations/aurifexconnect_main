package com.erp.Mapper.followup;


import com.erp.Dto.Request.FollowUpRequestDto;
import com.erp.Dto.Response.FollowUpResponseDto;
import com.erp.Model.FollowUpDetails;
import org.springframework.stereotype.Component;

@Component
public class FollowUpMapper {

//    public FollowUpDetails toEntity(FollowUpRequestDto dto) {
//        return FollowUpDetails.builder()
//                .id(dto.getId())
//                .leadId(dto.getLeadId())
////                .customerId(dto.getCustomerId())
//                .followUpType(dto.getFollowUpType())
//                .notes(dto.getNotes())
//                .nextFollowUpDate(dto.getNextFollowUpDate())
//                .nextFollowUpTime(dto.getNextFollowUpTime())
//                .status(dto.getStatus())
//                .completionDate(dto.getCompletionDate())
//                .build();
//    }
//
//    public void updateEntity(FollowUpDetails entity, FollowUpRequestDto dto) {
//        entity.setLeadId(dto.getLeadId());
//        entity.setCustomerId(dto.getCustomerId());
//        entity.setFollowUpType(dto.getFollowUpType());
//        entity.setNotes(dto.getNotes());
//        entity.setNextFollowUpDate(dto.getNextFollowUpDate());
//        entity.setNextFollowUpTime(dto.getNextFollowUpTime());
//        entity.setStatus(dto.getStatus());
//        entity.setCompletionDate(dto.getCompletionDate());
//    }

    public FollowUpDetails toEntityDto(FollowUpRequestDto dto)
    {
        FollowUpDetails response = new FollowUpDetails();

        response.setId(dto.getId());
        response.setLeadId(dto.getLeadId());
        response.setQuotationId(dto.getQuotationId());
        response.setFollowUpType(dto.getFollowUpType());
        response.setStatus(dto.getStatus());
        response.setNotes(dto.getNotes());
        response.setNextFollowupDate(dto.getNextFollowupDate());
        response.setNextFollowupTime(dto.getNextFollowupTime());
        response.setLostReason(dto.getLostReason());

        return response;
    }

    public FollowUpResponseDto toResponseDto(FollowUpDetails dto)
    {
        FollowUpResponseDto response = new FollowUpResponseDto();

        response.setId(dto.getId());
        response.setLeadId(dto.getLeadId());
        response.setQuotationId(dto.getQuotationId());
        response.setFollowUpType(dto.getFollowUpType());
        response.setStatus(dto.getStatus());
        response.setNotes(dto.getNotes());
        response.setNextFollowupDate(dto.getNextFollowupDate());
        response.setNextFollowupTime(dto.getNextFollowupTime());
        response.setLostReason(dto.getLostReason());
        response.setCreatedAt(dto.getCreatedAt());
        response.setUpdatedAt(dto.getUpdatedAt());

        return response;
    }
}
