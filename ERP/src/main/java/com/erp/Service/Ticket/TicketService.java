package com.erp.Service.Ticket;

import com.erp.Dto.Request.TicketRequestDTO;
import com.erp.Dto.Request.TicketSearchRequest;
import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Dto.Response.TicketSearchResponse;

import java.util.List;
import java.util.Map;

public interface TicketService {

    TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO);

    //TicketResponseDTO updateTicket(TicketRequestDTO ticketRequestDTO);
//
//    TicketResponseDTO getTicketBySupportId(Long supportId);
//
//    List<TicketResponseDTO> getAllTickets(Map<String, Object> filters);
//
//
//    List<TicketResponseDTO> searchTickets(String columnName, String value);

    void deleteTicketBySupportId(Long supportId);

    TicketSearchResponse searchTickets(TicketSearchRequest request);


}
