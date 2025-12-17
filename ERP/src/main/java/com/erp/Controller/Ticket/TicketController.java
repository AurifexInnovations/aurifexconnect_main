package com.erp.Controller.Ticket;

import com.erp.Dto.Request.TicketRequestDTO;
import com.erp.Dto.Request.TicketSearchRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Dto.Response.TicketSearchResponse;

import com.erp.Dto.Response.TicketViewDTO;
import com.erp.Service.Ticket.customerHelpTicket.TicketService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/Create")
    public ResponseEntity<ResponseStructure<TicketResponseDTO>> createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {
        log.info("Controller: createTicket called for customerId={}", ticketRequestDTO.getCustomerId());
        TicketResponseDTO response = ticketService.createTicket(ticketRequestDTO);
        return ResponseBuilder.success(HttpStatus.CREATED, "Ticket Created & Task Created !!", response);
    }

    @DeleteMapping
    public ResponseEntity<ResponseStructure<String>> deleteTicketBySupportId(@RequestParam Long ticketId) {
        log.info("Controller: deleteTicketBySupportId called for ticketId={}", ticketId);
        ticketService.deleteTicketBySupportId(ticketId);
        return ResponseBuilder.success(HttpStatus.OK, "Deleted Ticket With Id :"+ticketId, "DELETE");
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseStructure<TicketSearchResponse>> searchTickets(@RequestBody TicketSearchRequest request) {
        log.info("Received ticket search request: {}", request);
        TicketSearchResponse response = ticketService.searchTickets(request);
        return ResponseBuilder.success(HttpStatus.OK, "Ticket Searched", response);
    }

    @GetMapping()
    public ResponseEntity<ResponseStructure<ResultDto<TicketResponseDTO>>> allTickets(){
        log.info("Received All Tickets");
        ResultDto<TicketResponseDTO> resultDto = ticketService.getAll();
        return ResponseBuilder.success(HttpStatus.OK, "All Tickets", resultDto);
    }

    @GetMapping("/dash")
    public ResponseEntity<ResponseStructure<ResultDto<TicketViewDTO>>> fetchAllTickets(){
        log.info("Received All Tickets for Dashboard");
        ResultDto<TicketViewDTO> resultDto = ticketService.getAllTickets();
        return ResponseBuilder.success(HttpStatus.OK, "All Tickets for Dashboard", resultDto);
    }
}
