package com.erp.Service.Ticket.customerHelpTicket;

import com.erp.Dto.Request.TicketRequestDTO;
import com.erp.Dto.Request.TicketSearchRequest;
import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Dto.Response.TicketSearchResponse;
import com.erp.Enum.TicketStatus;
import com.erp.Mapper.Ticket.TicketMapper;
import com.erp.Model.Ticket;
import com.erp.Repository.Ticket.TicketRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO) {
        Long ticketId = ticketRequestDTO.getId();
        log.info("Add/Update ticket request received :: ticketId={}, customerId={}", ticketId, ticketRequestDTO.getCustomerId());

        try {
            Ticket ticket = null;

            if (ticketId != null && ticketRepository.existsById(ticketId)) {

                log.info("Existing ticket found. Updating ticket with id={}", ticketId);
                ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                        new RuntimeException("Ticket not found with id=" + ticketId));

                if(TicketStatus.CLOSED.equals(ticket.getTicketStatus())){
                    ticket.setResolvedAt(LocalDateTime.now());
                }
                ticketMapper.mapToTicketEntity(ticketRequestDTO, ticket);
            } else {
                log.info("No existing ticket found. Creating a new ticket for customerId={}", ticketRequestDTO.getCustomerId());


                ticket = ticketMapper.mapToTicket(ticketRequestDTO);
            }

            ticket.setCreatedAt(LocalDateTime.now());
            ticketRepository.save(ticket);
            log.info("Ticket saved successfully with id={}", ticket.getId());

            return ticketMapper.mapToTicketResponse(ticket);

        } catch (Exception e) {
            log.error("Error while creating/updating ticket for id={} :: {}", ticketId, e.getMessage(), e);
            throw new RuntimeException("Failed to create or update ticket");
        }
    }

    @Override
    public TicketSearchResponse searchTickets(TicketSearchRequest request) {
        log.info("Searching tickets with filters={}, sortBy={}, direction={}, page={}, size={}",
                request.getFilters(), request.getSortBy(), request.getSortDirection(), request.getPage(), request.getSize());

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // --- Create main query ---
        CriteriaQuery<Ticket> cq = cb.createQuery(Ticket.class);
        Root<Ticket> root = cq.from(Ticket.class);

        List<Predicate> predicates = buildPredicates(request.getFilters(), cb, root);

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        // --- Sorting ---
        if (request.getSortBy() != null && !request.getSortBy().isEmpty()) {
            Path<Object> sortPath = root.get(request.getSortBy());
            cq.orderBy("DESC".equalsIgnoreCase(request.getSortDirection())
                    ? cb.desc(sortPath)
                    : cb.asc(sortPath));
        }

        TypedQuery<Ticket> query = entityManager.createQuery(cq);

        // --- Pagination ---
        int page = request.getPage();
        int size = request.getSize();
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<Ticket> resultList = query.getResultList();

        // --- Count Query for Total Elements ---
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Ticket> countRoot = countQuery.from(Ticket.class);
        countQuery.select(cb.count(countRoot))
                .where(cb.and(buildPredicates(request.getFilters(), cb, countRoot).toArray(new Predicate[0])));
        long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        // --- Build Response ---
        TicketSearchResponse response = new TicketSearchResponse();
        response.setTickets(ticketMapper.mapToTicketResponse(resultList));
        response.setTotalElements(totalElements);
        response.setTotalPages((int) Math.ceil((double) totalElements / size));
        response.setCurrentPage(page);
        response.setPageSize(size);

        log.info("Fetched {} tickets, totalElements={}, totalPages={}", resultList.size(), totalElements, response.getTotalPages());
        return response;
    }

    private List<Predicate> buildPredicates(Map<String, Object> filters, CriteriaBuilder cb, Root<Ticket> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (filters == null) return predicates;

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value != null && !value.toString().trim().isEmpty()) {
                String lowerKey = key.toLowerCase();
                String searchValue = "%" + value.toString().toLowerCase() + "%";

                switch (lowerKey) {
                    case "priority":
                        predicates.add(cb.like(cb.lower(root.get("priority")), searchValue));
                        break;
                    case "status":
                        predicates.add(cb.like(cb.lower(root.get("status")), searchValue));
                        break;
                    case "customerlocation":
                        predicates.add(cb.like(cb.lower(root.get("customerLocation")), searchValue));
                        break;
                    case "technicianid":
                        predicates.add(cb.equal(root.get("technicianId"), value));
                        break;
                    case "customerid":
                        predicates.add(cb.equal(root.get("customerId"), value));
                        break;
                    case "taskid":
                        predicates.add(cb.equal(root.get("taskId"), value));
                        break;
                    default:
                        log.warn("Ignoring unknown filter key: {}", key);
                }
            }
        }
        return predicates;
    }

    @Override
    @Transactional
    public void deleteTicketBySupportId(Long ticketId) {
        log.info("Deleting ticket with ticketId={}", ticketId);
        try {
            if (ticketRepository.existsById(ticketId)) {
                ticketRepository.deleteById(ticketId);
                log.info("Ticket deleted successfully with ticketId={}", ticketId);
            } else {
                log.warn("Ticket not found with ticketId={}", ticketId);
                throw new RuntimeException("Ticket not found with ticketId=" + ticketId);
            }
        } catch (Exception e) {
            log.error("Error deleting ticket with supportId={}: {}", ticketId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete ticket");
        }
    }


}