package com.erp.Service.Ticket.internal;

import com.erp.Dto.Request.InternalTicketRequestDto;
import com.erp.Dto.Response.InternalTicketResponse;
import com.erp.Dto.Response.InternalTicketResponseDto;
import com.erp.Dto.Response.PaginationResult;
import com.erp.Model.InternalTicket;
import com.erp.Projection.InternalTicketProjection;

import java.util.List;
import java.util.Optional;

public interface InternalTicketService {

    InternalTicketResponse createTicket(InternalTicketRequestDto requestDto);


    InternalTicket getTicketById(Long id);

    PaginationResult<InternalTicketProjection> getAllTicketsWithPagination(int limit, int offset);
    String deleteTicketById(Long id );

}
