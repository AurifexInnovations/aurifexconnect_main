package com.erp.Service.Ticket;

import com.erp.Dto.Request.TicketRequestDTO;
import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Mapper.Ticket.TicketMapper;
import com.erp.Model.Ticket;
import com.erp.Repository.Ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional
    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO) {
        log.info("Creating ticket for customerId={} and taskId={}", ticketRequestDTO.getCustomerId(), ticketRequestDTO.getTaskId());
        try {
            Ticket ticket = ticketMapper.mapToTicket(ticketRequestDTO);
            ticketRepository.save(ticket);
            log.info("Ticket created successfully with supportId={}", ticket.getId());
            return ticketMapper.mapToTicketResponse(ticket);
        } catch (Exception e) {
            log.error("Error creating ticket for customerId={}: {}", ticketRequestDTO.getCustomerId(), e.getMessage(), e);
            throw new RuntimeException("Failed to create ticket");
        }
    }

    @Override
    @Transactional
    public TicketResponseDTO updateTicket(TicketRequestDTO ticketRequestDTO) {
        log.info("Updating ticket with supportId={}", ticketRequestDTO.getId());
        try {
            Optional<Ticket> optionalTicket = ticketRepository.findById(ticketRequestDTO.getId());
            if (optionalTicket.isPresent()) {
                Ticket ticket = optionalTicket.get();
                ticketMapper.mapToTicketEntity(ticketRequestDTO, ticket);
                ticketRepository.save(ticket);
                log.info("Ticket updated successfully with supportId={}", ticket.getId());
                return ticketMapper.mapToTicketResponse(ticket);
            } else {
                log.warn("Ticket not found with supportId={}", ticketRequestDTO.getId());
                throw new RuntimeException("Ticket not found with supportId=" + ticketRequestDTO.getId());
            }
        } catch (Exception e) {
            log.error("Error updating ticket with supportId={}: {}", ticketRequestDTO.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update ticket");
        }
    }

    @Override
    public TicketResponseDTO getTicketBySupportId(Long supportId) {
        log.info("Fetching ticket with supportId={}", supportId);
        try {
            return ticketRepository.findById(supportId)
                    .map(ticketMapper::mapToTicketResponse)
                    .orElseThrow(() -> {
                        log.warn("Ticket not found with supportId={}", supportId);
                        return new RuntimeException("Ticket not found with supportId=" + supportId);
                    });
        } catch (Exception e) {
            log.error("Error fetching ticket with supportId={}: {}", supportId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch ticket");
        }
    }

    @Override
    public List<TicketResponseDTO> getAllTickets(Map<String, Object> filters) {
        log.info("Fetching all tickets with filters={}", filters);
        try {
            List<Ticket> tickets = ticketRepository.findAll();
            log.info("Fetched {} tickets", tickets.size());
            return ticketMapper.mapToTicketResponse(tickets);
        } catch (Exception e) {
            log.error("Error fetching tickets: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch tickets");
        }
    }

    @Override
    @Transactional
    public void deleteTicketBySupportId(Long supportId) {
        log.info("Deleting ticket with supportId={}", supportId);
        try {
            if (ticketRepository.existsById(supportId)) {
                ticketRepository.deleteById(supportId);
                log.info("Ticket deleted successfully with supportId={}", supportId);
            } else {
                log.warn("Ticket not found with supportId={}", supportId);
                throw new RuntimeException("Ticket not found with supportId=" + supportId);
            }
        } catch (Exception e) {
            log.error("Error deleting ticket with supportId={}: {}", supportId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete ticket");
        }
    }

    @Override
    public List<TicketResponseDTO> searchTickets(String columnName, String value) {
        log.info("Searching tickets by {}={}", columnName, value);
        try {
            List<Ticket> tickets;
            switch (columnName.toLowerCase()) {
                case "priority":
                    tickets = ticketRepository.findByPriority(value);
                    break;
                case "status":
                    tickets = ticketRepository.findByStatus(value);
                    break;
                case "customerlocation":
                    tickets = ticketRepository.findByCustomerLocation(value);
                    break;
                default:
                    log.warn("Unknown column {}. Fetching all tickets", columnName);
                    tickets = ticketRepository.findAll();
            }
            log.info("Found {} tickets for {}={}", tickets.size(), columnName, value);
            return ticketMapper.mapToTicketResponse(tickets);
        } catch (Exception e) {
            log.error("Error searching tickets by {}={}: {}", columnName, value, e.getMessage(), e);
            throw new RuntimeException("Failed to search tickets");
        }
    }
}