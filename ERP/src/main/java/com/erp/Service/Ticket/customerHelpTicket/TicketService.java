package com.erp.Service.Ticket.customerHelpTicket;

import com.erp.Dto.Request.TicketRequestDTO;
import com.erp.Dto.Request.TicketSearchRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Dto.Response.TicketSearchResponse;
import com.erp.Dto.Response.TicketViewDTO;

public interface TicketService {

    TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO);

    void deleteTicketBySupportId(Long ticketId);

    TicketSearchResponse searchTickets(TicketSearchRequest request);

    ResultDto<TicketResponseDTO> getAll();

    ResultDto<TicketViewDTO> getAllTickets();
}
