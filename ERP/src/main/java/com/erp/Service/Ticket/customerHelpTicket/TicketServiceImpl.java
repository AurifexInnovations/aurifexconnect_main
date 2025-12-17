package com.erp.Service.Ticket.customerHelpTicket;

import com.erp.CustomRepository.InventoryCustomRepository;
import com.erp.CustomRepository.TaskTechnicianCustomRepository;
import com.erp.Dto.Request.TicketRequestDTO;
import com.erp.Dto.Request.TicketSearchRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Dto.Response.TicketSearchResponse;
import com.erp.Dto.Response.TicketViewDTO;
import com.erp.Enum.TaskStatus;
import com.erp.Enum.TicketStatus;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.Task.TaskNoFoundException;
import com.erp.Mapper.TaskMapper.TaskDetailsMapper;
import com.erp.Mapper.TaskMapper.TaskMapper;
import com.erp.Mapper.Ticket.TicketMapper;
import com.erp.Model.*;
import com.erp.Repository.Feedback.FeedbackRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.Inventory.InventoryRepositoryV2;
import com.erp.Repository.Task.*;
import com.erp.Repository.Ticket.TicketRepository;
import com.erp.Repository.Utility.FileRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.TaskService.TaskService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TaskRepository taskRepository;
    private final TaskScheduleRepository taskScheduleRepository;
    private final TaskServiceMapperRepository taskServiceMapperRepository;
    private final TechnicianTaskMapperRepository technicianTaskMapperRepository;
    private final TaskMaterialRepository taskMaterialRepository;
    private final TaskMapper taskMapper;
    private final TaskDetailsMapper taskDetailsMapper;
    private final FeedbackRepository feedbackRepository;
    private final TaskTechnicianCustomRepository taskTechnicianCustomRepository;
    private final InventoryCustomRepository inventoryCustomRepository;
    private final FileRepository fileRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryRepositoryV2 inventoryRepositoryV2;
    private final UserIdentity userIdentity;

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
                        new ResourceNotFoundException("Ticket not found with id=" + ticketId));

                if(TicketStatus.CLOSED.equals(ticket.getTicketStatus())){
                    ticket.setResolvedAt(LocalDateTime.now());
                }
                ticketMapper.mapToTicketEntity(ticketRequestDTO, ticket);
            } else {
                log.info("No existing ticket found. Creating a new ticket for customerId={}", ticketRequestDTO.getCustomerId());

                Task old = taskRepository.findById(ticketRequestDTO.getTaskId())
                        .orElseThrow(() -> new TaskNoFoundException("Task Not Found !!"));

                Task task = new Task();

                GenericUser genericUser = userIdentity.getCurrentUser();
                task.setTaskName(old.getTaskName());
                task.setCustomerId(old.getCustomerId());
                task.setTaskCategory(old.getTaskCategory());
                task.setTaskDetails(old.getTaskDetails());
                task.setTaskStatus(old.getTaskStatus());
                task.setCreatedBy(genericUser.getId());
                task.setLatitude(old.getLatitude());
                task.setLongitude(old.getLongitude());

                TaskSchedule taskScheduleOld = taskScheduleRepository.findByTaskId(ticketRequestDTO.getTaskId());
                List<TaskServiceMapper> taskServices = new ArrayList<>();
                List<TechnicianTaskMapper> technicianMappers = new ArrayList<>();
                List<TaskMaterial> materials = new ArrayList<>();

                Task saved = taskRepository.save(task);
                TaskSchedule taskSchedule = new TaskSchedule();
                taskSchedule.setTaskId(saved.getTaskId());
                taskSchedule.setAssignedTime(ticketRequestDTO.getAssignedTime());
                taskSchedule.setAssignedDate(ticketRequestDTO.getAssignedDate());
                taskSchedule.setGoogleLocationLink(taskScheduleOld.getGoogleLocationLink());
                taskSchedule.setFieldType(taskScheduleOld.getFieldType());
                taskSchedule.setServiceLocation(taskScheduleOld.getServiceLocation());

                for(TaskServiceMapper taskServiceMapper : taskServiceMapperRepository.findByTaskId(ticketRequestDTO.getTaskId())){
                    TaskServiceMapper newObj = new TaskServiceMapper();

                    newObj.setTaskId(saved.getTaskId());
                    newObj.setServiceId(taskServiceMapper.getServiceId());

                    taskServices.add(newObj);
                }

                for(TechnicianTaskMapper technicianTaskMapper : technicianTaskMapperRepository.getTechnitiansByTaskId(ticketRequestDTO.getTaskId())){
                    TechnicianTaskMapper newobj = new TechnicianTaskMapper();

                    newobj.setTaskId(saved.getTaskId());
                    newobj.setTechnicianId(ticketRequestDTO.getTechnicianId());

                    technicianMappers.add(newobj);
                }

                for(TaskMaterial taskMaterial : taskMaterialRepository.findByTaskId(ticketRequestDTO.getTaskId())){
                    TaskMaterial newObj = new TaskMaterial();

                    newObj.setTaskId(saved.getTaskId());
                    newObj.setMaterialId(taskMaterial.getMaterialId());
                    newObj.setQuantity(taskMaterial.getQuantity());
                    newObj.setUnit(taskMaterial.getUnit());
                    newObj.setIsUsed(taskMaterial.getIsUsed());

                    materials.add(newObj);
                }

                taskScheduleRepository.save(taskSchedule);
                taskMaterialRepository.saveAll(materials);
                technicianTaskMapperRepository.saveAll(technicianMappers);
                taskServiceMapperRepository.saveAll(taskServices);

                ticket = ticketMapper.mapToTicket(ticketRequestDTO);
            }

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

    @Override
    public ResultDto<TicketResponseDTO> getAll() {

        ResultDto<TicketResponseDTO> resultDto = new ResultDto<>();

        List<Ticket> tickets = ticketRepository.findAll();

        List<TicketResponseDTO> result = ticketMapper.mapToListTicketResponse(tickets);

        resultDto.setResults(result);
        resultDto.setCount(result.size());

        return resultDto;
    }

    @Override
    public ResultDto<TicketViewDTO> getAllTickets() {
        List<TicketViewDTO> list = ticketRepository.findAllTicketViews();
        ResultDto<TicketViewDTO> resultDto = new ResultDto<>();

        resultDto.setResults(list);
        resultDto.setCount(list.size());

        return resultDto;
    }
}