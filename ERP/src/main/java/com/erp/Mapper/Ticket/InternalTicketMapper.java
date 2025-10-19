package com.erp.Mapper.Ticket;

import com.erp.Dto.Request.InternalTicketRequestDto;
import com.erp.Dto.Response.InternalTicketResponseDto;
import com.erp.Model.InternalTicket;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class InternalTicketMapper {


    public InternalTicket map(InternalTicketRequestDto request) {
        return update(null, request);
    }


    public InternalTicket update(InternalTicket entity, InternalTicketRequestDto request) {
        if (Objects.isNull(entity)) {
            entity = new InternalTicket();
        }

        if (Objects.nonNull(request)) {
            entity.setTitle(request.getTitle());
            entity.setPriority(request.getPriority());
            entity.setStatus(request.getStatus());
            entity.setAssignedTo(request.getAssignedTo());
        }

        return entity;
    }


    public InternalTicketResponseDto mapToDto(InternalTicket ticket) {
        InternalTicketResponseDto dto = new InternalTicketResponseDto();
        dto.setId(ticket.getId());
        dto.setTitle(ticket.getTitle());
        dto.setPriority(ticket.getPriority());
        dto.setStatus(ticket.getStatus());
        dto.setAssignedTo(ticket.getAssignedTo());
        dto.setCreatedBy(ticket.getCreatedBy());
        dto.setCreatedDate(ticket.getCreatedDate());
        dto.setLastUpdated(ticket.getLastUpdated());
        return dto;
    }

    public List<InternalTicketResponseDto> mapToDtoList(List<InternalTicket> tickets) {
        return tickets.stream().map(this::mapToDto).collect(Collectors.toList());
    }

}
