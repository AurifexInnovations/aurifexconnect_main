package com.erp.Mapper.Ticket;

import com.erp.Dto.Request.TicketRequestDTO;
import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Model.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface TicketMapper {

    Ticket mapToTicket(TicketRequestDTO ticketRequestDTO);

    void mapToTicketEntity(TicketRequestDTO ticketRequestDTO, @MappingTarget Ticket ticket);

    TicketResponseDTO mapToTicketResponse(Ticket ticket);

    List<TicketResponseDTO> mapToTicketResponse(List<Ticket> ticketList);

    List<TicketResponseDTO> mapToListTicketResponse(List<Ticket> tickets);
}