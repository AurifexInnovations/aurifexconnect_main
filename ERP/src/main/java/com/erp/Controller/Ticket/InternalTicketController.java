package com.erp.Controller.Ticket;

import com.erp.Dto.Request.InternalTicketRequestDto;
import com.erp.Dto.Response.InternalTicketResponse;
import com.erp.Dto.Response.InternalTicketResponseDto;
import com.erp.Dto.Response.PaginationResult;
import com.erp.Model.InternalTicket;
import com.erp.Projection.InternalTicketProjection;
import com.erp.Service.Ticket.internal.InternalTicketService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/internal-ticket")
@RequiredArgsConstructor
public class InternalTicketController {

    private final InternalTicketService internalTicketService;


    @PostMapping
    public ResponseEntity<ResponseStructure<InternalTicketResponse>> createOrUpdateTicket(
            @RequestBody InternalTicketRequestDto requestDto) {

        log.info("Into [InternalTicketController] [createOrUpdateTicket] :: request :: {}", requestDto);

        InternalTicketResponse response = internalTicketService.createTicket(requestDto);

        log.info("Exit [InternalTicketController] [createOrUpdateTicket] :: response :: {}", response);
        return ResponseBuilder.success(HttpStatus.CREATED, "Ticket created or updated successfully", response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<InternalTicket>> getTicketById(@PathVariable("id") Long id) {
        log.info("Into [InternalTicketController] [getTicketById] :: id :: {}", id);

        InternalTicket ticket = internalTicketService.getTicketById(id);

        log.info("Exit [InternalTicketController] [getTicketById] :: response :: {}", ticket);
        return ResponseBuilder.success(HttpStatus.OK, "Ticket fetched successfully", ticket);
    }


    @GetMapping
    public ResponseEntity<ResponseStructure<PaginationResult<InternalTicketProjection>>> getPaginatedTickets(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        log.info("Into [InternalTicketController] [getPaginatedTickets] :: limit={} offset={}", limit, offset);
        PaginationResult<InternalTicketProjection> response = internalTicketService.getAllTicketsWithPagination(limit, offset);
        log.info("Exit [InternalTicketController] [getPaginatedTickets] :: count={}", response.getCount());

        return ResponseBuilder.success(HttpStatus.OK, "Tickets fetched successfully", response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteTicketById(@PathVariable("id") Long id) {
        log.info("Into [InternalTicketController] [deleteTicketById] :: id :: {}", id);

       internalTicketService.deleteTicketById(id);

        log.info("Exit [InternalTicketController] [deleteTicketById] :: id :: {}", id);
        return ResponseBuilder.success(HttpStatus.OK, "Ticket deleted successfully", "");
    }

}
