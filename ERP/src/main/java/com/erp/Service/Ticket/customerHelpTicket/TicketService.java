package com.erp.Service.Ticket.customerHelpTicket;

import com.erp.Dto.Request.TicketRequestDTO;
import com.erp.Dto.Request.TicketSearchRequest;
import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Dto.Response.TicketSearchResponse;

public interface TicketService {

    TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO);

    void deleteTicketBySupportId(Long ticketId);

    TicketSearchResponse searchTickets(TicketSearchRequest request);


}
