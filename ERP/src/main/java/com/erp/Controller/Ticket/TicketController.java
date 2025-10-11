package com.erp.Controller.Ticket;

import com.erp.Dto.Request.TicketRequestDTO;
import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Service.Ticket.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {
        log.info("Controller: createTicket called for customerId={}", ticketRequestDTO.getCustomerId());
        TicketResponseDTO response = ticketService.createTicket(ticketRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/Update")
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @PathVariable Long supportId,
            @RequestBody TicketRequestDTO ticketRequestDTO) {
        log.info("Controller: updateTicket called for supportId={}", supportId);
        ticketRequestDTO.setId(supportId);
        TicketResponseDTO response = ticketService.updateTicket(ticketRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{supportId}")
    public ResponseEntity<TicketResponseDTO> getTicketBySupportId(@PathVariable Long supportId) {
        log.info("Controller: getTicketBySupportId called for supportId={}", supportId);
        TicketResponseDTO response = ticketService.getTicketBySupportId(supportId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(@RequestParam(required = false) Map<String, Object> filters) {
        log.info("Controller: getAllTickets called with filters={}", filters);
        List<TicketResponseDTO> response = ticketService.getAllTickets(filters);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{supportId}")
    public ResponseEntity<String> deleteTicketBySupportId(@PathVariable Long supportId) {
        log.info("Controller: deleteTicketBySupportId called for supportId={}", supportId);
        ticketService.deleteTicketBySupportId(supportId);
        return ResponseEntity.ok("Ticket deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<TicketResponseDTO>> searchTickets(
            @RequestParam String columnName,
            @RequestParam String value) {
        log.info("Controller: searchTickets called with {}={}", columnName, value);
        List<TicketResponseDTO> response = ticketService.searchTickets(columnName, value);
        return ResponseEntity.ok(response);
    }
}
