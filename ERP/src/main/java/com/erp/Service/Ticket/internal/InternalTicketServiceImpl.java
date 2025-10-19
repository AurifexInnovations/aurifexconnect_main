package com.erp.Service.Ticket.internal;


import com.erp.Dto.Request.InternalTicketRequestDto;
import com.erp.Dto.Response.InternalTicketResponse;
import com.erp.Dto.Response.InternalTicketResponseDto;
import com.erp.Dto.Response.PaginationResult;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.Ticket.InternalTicketMapper;
import com.erp.Model.GenericUser;
import com.erp.Model.InternalTicket;
import com.erp.Projection.InternalTicketProjection;
import com.erp.Repository.InternalTicketRepository;
import com.erp.Security.util.UserIdentity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalTicketServiceImpl implements InternalTicketService {

    private final InternalTicketRepository internalTicketRepository;
    private final InternalTicketMapper internalTicketMapper;
    private static  final String TICKET_CREATION_MESSAGE= "Ticket created successfully";
    private static  final String TICKET_UPDATED_MESSAGE= "Ticket updated successfully";

    private  final UserIdentity  userIdentity;

    @Override
    public InternalTicketResponse createTicket(InternalTicketRequestDto requestDto) {

        if (Objects.isNull(requestDto)) {
            log.error("Cannot create or update ticket — request body is null");
            throw new ResourceNotFoundException("Request cannot be null");
        }

        GenericUser user  = userIdentity.getCurrentUser();
        InternalTicketResponse internalTicketResponse = new InternalTicketResponse();

        internalTicketResponse.setMessage(TICKET_CREATION_MESSAGE);
        if (Objects.nonNull(requestDto.getId())) {
            log.info("Updating internal ticket with ID: {}", requestDto.getId());

            InternalTicket existingTicket = internalTicketRepository.findById(requestDto.getId())
                    .orElseGet(() -> {
                        log.warn("Ticket not found with ID: {}, creating a new one", requestDto.getId());
                        return new InternalTicket();
                    });

            internalTicketMapper.update(existingTicket, requestDto);
            existingTicket.setLastUpdated(LocalDateTime.now());
            InternalTicket updatedTicket = internalTicketRepository.save(existingTicket);
            InternalTicketResponseDto internalTicketResponseDto =  internalTicketMapper.mapToDto(updatedTicket);
            internalTicketResponse.setMessage(TICKET_UPDATED_MESSAGE);
            internalTicketResponse.setDto(internalTicketResponseDto);
            log.info("Internal ticket updated successfully with ID: {}", updatedTicket.getId());

            return internalTicketResponse;

        } else {

            log.info("Creating new internal ticket with title: {}", requestDto.getTitle());

            InternalTicket newTicket = internalTicketMapper.map(requestDto);
            newTicket.setCreatedBy(user.getId());
            newTicket.setCreatedDate(LocalDateTime.now());
            InternalTicket savedTicket = internalTicketRepository.save(newTicket);

            log.info("Internal ticket created successfully with ID: {}", savedTicket.getId());
            InternalTicketResponseDto internalTicketResponseDto =  internalTicketMapper.mapToDto(savedTicket);
            internalTicketResponse.setMessage(TICKET_CREATION_MESSAGE);
            internalTicketResponse.setDto(internalTicketResponseDto);
            return internalTicketResponse;
        }
    }



    @Override
    public InternalTicket getTicketById(Long id) {
        log.info("Fetching internal ticket by ID: {}", id);

        Optional<InternalTicket> ticketOptional =
                internalTicketRepository.findById(id);

        if (ticketOptional.isPresent()) {
            log.info("Internal ticket found with ID: {}", id);
            return ticketOptional.get();
        } else {
            log.warn("No internal ticket found with ID: {}, returning empty InternalTicket object", id);
            return new InternalTicket();
        }
    }



    @Override
    public PaginationResult<InternalTicketProjection> getAllTicketsWithPagination(int limit, int offset) {
        log.info("Fetching paginated internal tickets with limit: {} and offset: {}", limit, offset);

        List<InternalTicketProjection> tickets = internalTicketRepository.findAllTicketsWithUserNames(limit, offset);

        long totalCount = internalTicketRepository.getTotalTicketCount();



        PaginationResult<InternalTicketProjection> result = new PaginationResult<>();
        result.setCount(totalCount);
        result.setResults(tickets);

        log.info("Fetched {} tickets out of total {}", tickets.size(), totalCount);

        return result;
    }




    @Override
    public String deleteTicketById(Long id) {
        log.info("Attempting to delete internal ticket with ID: {}", id);

        if (!internalTicketRepository.existsById(id)) {
            log.error("Cannot delete — Ticket not found with ID: {}", id);
            throw new ResourceNotFoundException("Ticket not found with ID: " + id);
        }
        internalTicketRepository.deleteById(id);
        log.info("Internal ticket deleted successfully with ID: {}", id);
        return "Ticket deleted successfully";
    }
}
