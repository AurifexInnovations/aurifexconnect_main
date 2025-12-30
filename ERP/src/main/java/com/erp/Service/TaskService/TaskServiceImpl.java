package com.erp.Service.TaskService;

import com.erp.Config.AmazonS3Config;
import com.erp.CustomRepository.InventoryCustomRepository;
import com.erp.CustomRepository.TaskTechnicianCustomRepository;
import com.erp.Dto.Request.*;
import com.erp.Dto.Response.*;
import com.erp.Dto.Response.TechnicianResponse;
import com.erp.Enum.TaskStatus;
import com.erp.Events.Invoice.InvoiceConfirmedEvent;
import com.erp.Exception.BadRequestException;
import com.erp.Exception.DBReltedException;
import com.erp.Exception.GlobalMessageExceptionHandler;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.Task.TaskNoFoundException;

import com.erp.Mapper.TaskMapper.TaskDetailsMapper;
import com.erp.Mapper.TaskMapper.TaskMapper;

import com.erp.Model.Task;

import com.erp.Model.*;
import com.erp.Projection.*;
import com.erp.Repository.Feedback.FeedbackRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.Inventory.InventoryRepositoryV2;
import com.erp.Repository.Task.*;
import com.erp.Repository.Utility.FileRepository;
import com.erp.Security.util.UserIdentity;

import com.erp.Service.InventoryService.InventoryService;
import com.erp.Service.Otp.OtpService;
import com.erp.Service.Utility.FileService;
import com.erp.Utility.inerfaces.S3StorageService;
import com.erp.constants.FileUploadConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;


import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Value("${aws.s3.bucket}")
    private String bucket;

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
    private final S3StorageService s3StorageService;
    private final TaskDocumentsRepository taskDocumentsRepository;
    private final S3Presigner s3Presigner;

    @Lazy
    @Autowired
    private FileService fileService;

    private final UserIdentity userIdentity;

    private final OtpService otpService;



    @EventListener
    public void handleInvoiceConfirmed(InvoiceConfirmedEvent event) {
        Invoice invoice = event.getInvoice();
        GenericUser currentUser = userIdentity.getCurrentUser();

        // Create basic task with common fields
        Task task = new Task();
        task.setTaskName("New Task");
        task.setInvoiceId(invoice.getId());
        task.setCustomerId(invoice.getCustomerId());
        task.setCreatedAt(LocalDateTime.now());
        task.setTaskStatus(TaskStatus.PENDING);
        task.setCreatedBy(currentUser.getId());

        // Save basic task
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(TaskRequest taskRequest) {
        log.info("Into [TaskServiceImpl]  [addTask] Into add task ");

        if (taskRequest == null) {
            throw new GlobalMessageExceptionHandler("Task request cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Task task;

        try {

            if (Objects.isNull(taskRequest)) {
                throw new GlobalMessageExceptionHandler("Add Task  request can not be empty", HttpStatus.BAD_REQUEST);
            }

            GenericUser currentUser = userIdentity.getCurrentUser();

            if (taskRequest.getTaskId() != null) {

                task = taskRepository.findById(taskRequest.getTaskId())
                        .orElseThrow(() -> new TaskNoFoundException("Task not found with Id: " + taskRequest.getTaskId()));

                taskMapper.mapToTaxEntity(taskRequest, task);
                task.setUpdatedBy(currentUser.getId());
                task.setUpdatedAt(LocalDateTime.now());

            } else {
                task = taskMapper.mapToTask(taskRequest);
                task.setCreatedBy(currentUser.getId());
                task.setCreatedAt(LocalDateTime.now());
            }

            task.setLatitude(taskRequest.getLatitude());
            task.setLongitude(taskRequest.getLongitude());
            task.setTaskDetails(taskRequest.getTaskDetails());
            task = taskRepository.save(task);
            taskRequest.setTaskId(task.getTaskId());
            addTaskSchedule(taskRequest);
            addServiceToTask(taskRequest);
            addTechniciansToTask(taskRequest);
            addMaterialsToTask(taskRequest);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Exit [TaskServiceImpl]  [addTask]");
        return taskDetailsMapper.mapToTaskResponse(task.getTaskId());
    }

    public String updateTaskLocation(Long taskId, TaskLocationUpdateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        task.setLatitude(request.getLatitude());
        task.setLongitude(request.getLongitude());
        task.setUpdatedAt((LocalDateTime.now()));

        taskRepository.save(task);
        return "Location updated successfully";
    }

    private void addTaskSchedule(TaskRequest taskRequest) {

        log.info("Into add  TaskSchedule...");

        TaskSchedule optionalTaskSchedule =
                taskScheduleRepository.findByTaskId(taskRequest.getTaskId());

        if (optionalTaskSchedule == null) {
            optionalTaskSchedule = new TaskSchedule();
        }

        optionalTaskSchedule = taskDetailsMapper.mapToTaskSchedule(taskRequest, optionalTaskSchedule);
        optionalTaskSchedule.setTaskStartTime(LocalTime.now());
        taskScheduleRepository.save(optionalTaskSchedule);

        log.info("TaskSchedule processed successfully");


    }


    public void addServiceToTask(TaskRequest taskRequest) {

        Long taskId = taskRequest.getTaskId();
        log.info("Initiating service update for Task ID: {}", taskId);
        try {

//            log.debug("Deleting existing service mappings for Task ID: {}", taskId);
//            taskServiceMapperRepository.deleteByTaskId(taskId);


            log.debug("Mapping new services for Task ID: {}", taskId);
            List<TaskServiceMapper> existing  = taskServiceMapperRepository.findByTaskId(taskId);

            Map<Long, TaskServiceMapper> existingMap =
                    existing.stream()
                            .collect(Collectors.toMap(
                                    m -> m.getServiceId(),
                                    Function.identity()
                            ));

            List<TaskServiceMapper> finalList = new ArrayList<>();

            for (Long serviceId : taskRequest.getServiceId()) {

                TaskServiceMapper mapper = existingMap.remove(serviceId);

                if (mapper == null) {
                    mapper = new TaskServiceMapper();
                    mapper.setTaskId(taskId);
                    mapper.setServiceId(serviceId);
                }

                finalList.add(mapper);
            }

            // delete only removed services
            taskServiceMapperRepository.deleteAll(existingMap.values());

            log.debug("Saving {} service mappings for Task ID: {}", finalList .size(), taskId);
            taskServiceMapperRepository.saveAll(finalList );

            log.info("Service update COMPLETED successfully for Task ID: {}", taskId);
        } catch (Exception e) {
            log.error("Error occurred while updating services for Task ID: {}", taskId, e);
            throw e;
        }

    }

    public void addTechniciansToTask(TaskRequest taskRequest) {

        Long taskId = taskRequest.getTaskId();
        log.info("Initiating technician update for Task ID: {}", taskId);

        try {
//            log.debug("Deleting existing technician mappings for Task ID: {}", taskId);
//            technicianTaskMapperRepository.deleteByTaskId(taskId);

            log.debug("Mapping new technicians for Task ID: {}", taskId);
            List<TechnicianTaskMapper> existing  = technicianTaskMapperRepository.findByTaskId(taskId);

            Map<Long, TechnicianTaskMapper> existingMap =
                    existing.stream()
                            .collect(Collectors.toMap(
                                    m -> m.getTechnicianId(),
                                    Function.identity()
                            ));

            List<TechnicianTaskMapper> finalList = new ArrayList<>();

            for (Long technicianId : taskRequest.getTechnicianId()) {

                TechnicianTaskMapper mapper = existingMap.remove(technicianId);

                if (mapper == null) {
                    mapper = new TechnicianTaskMapper();
                    mapper.setTaskId(taskId);
                    mapper.setTechnicianId(technicianId);
                }

                finalList.add(mapper);
            }

            technicianTaskMapperRepository.deleteAll(existingMap.values());

            log.debug("Saving {} technician mappings for Task ID: {}", finalList .size(), taskId);
            technicianTaskMapperRepository.saveAll(finalList );

            log.info("Technician update COMPLETED successfully for Task ID: {}", taskId);
        } catch (Exception e) {
            log.error("Error occurred while updating technicians for Task ID: {}", taskId, e);
            throw e;
        }
    }


    public void addMaterialsToTask(TaskRequest taskRequest) {

        Long taskId = taskRequest.getTaskId();
        log.info("Initiating material update for Task ID: {}", taskId);

        try {
//            log.debug("Deleting existing material mappings for Task ID: {}", taskId);
//            taskMaterialRepository.deleteByTaskId(taskId);

            log.debug("Mapping new materials for Task ID: {}", taskId);
            List<TaskMaterial> existing  = taskMaterialRepository.findByTaskId(taskId);

            Map<Long, TaskMaterial> existingMap =
                    existing.stream()
                            .collect(Collectors.toMap(
                                    m -> m.getMaterialId(),
                                    Function.identity()
                            ));

            List<TaskMaterial> finalList = new ArrayList<>();

            for (MaterialDto dto : taskRequest.getMaterialDto()) {

                TaskMaterial material = existingMap.remove(dto.getMaterialId());

                if (material == null) {
                    material = new TaskMaterial();
                    material.setTaskId(taskId);
                    material.setMaterialId(dto.getMaterialId());
                }

                material.setQuantity(dto.getQuantity());
                material.setUnit(dto.getUnit());
                material.setIsUsed(dto.getIsUsed());

                finalList.add(material);
            }


            taskMaterialRepository.deleteAll(existingMap.values());

            log.debug("Saving {} material mappings for Task ID: {}", finalList.size(), taskId);
            taskMaterialRepository.saveAll(finalList);

            log.info("Material update COMPLETED successfully for Task ID: {}", taskId);
        } catch (Exception e) {
            log.error("Error occurred while updating materials for Task ID: {}", taskId, e);
            throw e;
        }
    }

    @Override
    public List<GetAllTaskResponse> getAllTasks(Integer page, Integer size) {

        log.info("[TaskService] [getAllTaskRequests] Entering with page: {}, size: {}", page, size);

        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 10;

        try {
            int offset = page * size;

            List<GetAllTaskResponse> tasks = taskRepository.findTasksWithSchedule(size, offset);

            if (tasks == null || tasks.isEmpty()) {
                log.info("[TaskService] [getAllTaskRequests] No tasks found. Returning empty list.");
                return Collections.emptyList();
            }

            log.info("[TaskService] [getAllTaskRequests] Fetched {} tasks", tasks.size());
            return tasks;

        } catch (Exception ex) {
            log.error("[TaskService] [getAllTaskRequests] Error occurred while fetching tasks: {}", ex.getMessage(), ex);
            return Collections.emptyList();
        } finally {
            log.info("[TaskService] [getAllTaskRequests] Exiting method");
        }
    }


    @Override
    public ResultDto<GetAllTaskResponse> getAllTasks() {
        log.info("[TaskService] [getAllTaskRequests] Entering with page: {}, size: {}");

        try {

            List<GetAllTaskResponse> tasks = taskRepository.findAllTask();

            ResultDto<GetAllTaskResponse> resultDto = new ResultDto<>();

            resultDto.setResults(tasks != null ? tasks : List.of());
            resultDto.setCount(tasks != null ? tasks.size() : 0);

            log.info("[TaskService] [getAllTaskRequests] Fetched {} tasks", tasks.size());
            return resultDto;

        } catch (Exception ex) {
            log.error("[TaskService] [getAllTaskRequests] Error occurred while fetching tasks: {}", ex.getMessage(), ex);
            throw new RuntimeException("While Fetching Tasks Data");
        } finally {
            log.info("[TaskService] [getAllTaskRequests] Exiting method");
        }
    }

    @Override
    public boolean getTask(long taskId) {

        return taskRepository.findById(taskId).isPresent();
    }


    @Override
    public List<com.erp.Projection.TechnicianResponse> getTechnicians(TechnicianRequest technicianRequest) {

        log.info("[TaskService] [getTechnicians] Entered with request: {}", technicianRequest);

        List<com.erp.Projection.TechnicianResponse> technicianList;

        try {
            // Convert status string -> Boolean
            Boolean isActive = null;
            if (technicianRequest.getStatus() != null) {
                if ("active".equalsIgnoreCase(technicianRequest.getStatus())) {
                    isActive = Boolean.TRUE;
                } else if ("inactive".equalsIgnoreCase(technicianRequest.getStatus())) {
                    isActive = Boolean.FALSE;
                }
            }

            // ALWAYS call repository – your SQL handles NULL filters.
            technicianList = taskRepository.searchTasksWithScheduleAndTechnicians(
                    isActive,
                    technicianRequest.getCategory(),
                    technicianRequest.getDay(),
                    technicianRequest.getMonth(),
                    technicianRequest.getTaskId(),
                    technicianRequest.getTechnicianId(),
                    technicianRequest.getStartDate(),
                    technicianRequest.getEndDate()
            );

            log.info("[TaskService] [getTechnicians] Found {} technicians", technicianList.size());

        } catch (Exception e) {
            log.error("[TaskService] [getTechnicians] Error while fetching technicians", e);
            throw new ResourceNotFoundException(e.getMessage());
        }

        log.info("[TaskService] [getTechnicians] Exiting method");
        return technicianList;
    }


    @Override
    public List<TechnicianTaskProjection> getTechniciansByDateAndAssigenDate(TechnicianTaskRequest technicianTaskRequest) {

        List<TechnicianTaskProjection> technicianList = new ArrayList<>();

        try {
            log.info("Fetching technician tasks for technicianId={} on assignedDate={} with offset={} and size={}",
                    technicianTaskRequest.getTechnicianId(),
                    technicianTaskRequest.getAssignedDate(),
                    technicianTaskRequest.getOffset(),
                    technicianTaskRequest.getSize());

            technicianList = taskScheduleRepository.getTechnicianTasks(
                    technicianTaskRequest.getAssignedDate(),
                    technicianTaskRequest.getTechnicianId(),
                    technicianTaskRequest.getSize(),
                    technicianTaskRequest.getOffset()
            );

            log.info("Fetched {} technician tasks successfully", technicianList.size());

        } catch (Exception e) {
            log.error("Unexpected error while fetching technician tasks for technicianId={} on assignedDate={}",
                    technicianTaskRequest.getTechnicianId(),
                    technicianTaskRequest.getAssignedDate(), e);
            throw new RuntimeException("Unexpected error occurred", e);
        }

        return technicianList;
    }


    @Override
    public ResultDto<TechnicianLeaderboardDto> getTechnicianLeaderboard(PaginationRequest paginationRequest) {
        try {
            ResultDto<TechnicianLeaderboardDto> resultDto = new ResultDto<>();

            var leaderboardData = taskScheduleRepository.findTechnicianLeaderboard();
            log.info("Fetched {} technicians for leaderboard", leaderboardData.size());

            var materialData = taskScheduleRepository.findTechnicianMaterialUsage();
            log.info("Fetched {} material usage records", materialData.size());

            Map<Long, List<MaterialUsageDto>> materialsByTech = materialData.stream()
                    .collect(Collectors.groupingBy(
                            TechnicianMaterialProjection::getTechnicianId,
                            Collectors.mapping(m -> new MaterialUsageDto(
                                    m.getProductName(),
                                    m.getTotalQuantity(),
                                    m.getUnit()
                            ), Collectors.toList())
                    ));
            log.debug("Grouped materials by technician: {}", materialsByTech);

            List<TechnicianLeaderboardDto> result = leaderboardData.stream()
                    .map(t -> {
                        List<MaterialUsageDto> materials = materialsByTech.getOrDefault(t.getTechnicianId(), Collections.emptyList());
                        log.debug("Mapping technicianId={} with {} materials", t.getTechnicianId(), materials.size());
                        return new TechnicianLeaderboardDto(
                                t.getTechnicianId(),
                                t.getTechnicianName(),
                                t.getCompletedTasks(),
                                t.getAvgRating(),
                                t.getRank(),
                                materials
                        );
                    })
                    .collect(Collectors.toList());

            // -------------------------
            // APPLY PAGINATION HERE
            // -------------------------
            int page = paginationRequest.getPageNumber();
            int size = paginationRequest.getPageSize();
            int fromIndex = page * size;
            int toIndex = Math.min(fromIndex + size, result.size());

            List<TechnicianLeaderboardDto> paginated;

            if (fromIndex >= result.size()) {
                paginated = Collections.emptyList();
            } else {
                paginated = result.subList(fromIndex, toIndex);
            }

            resultDto.setResults(paginated);
            resultDto.setCount(paginated.size());

            log.info("Final leaderboard DTO list size: {}", result.size());
            return resultDto;

        } catch (Exception e) {
            log.error("Error fetching technician leaderboard", e);
            throw new GlobalMessageExceptionHandler(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<TechnicianLeaderboardDto> getTechnicianLeaderboard(String startDate, String endDate) {
        try {
            log.info("Fetching Technician Leaderboard from {} to {}", startDate, endDate);

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            var leaderboardData = taskScheduleRepository.findTechnicianLeaderboard(start, end);
            log.info("Fetched {} technicians for leaderboard", leaderboardData.size());

            var materialData = taskScheduleRepository.findTechnicianMaterialUsage(start, end);
            log.info("Fetched {} material usage records", materialData.size());

            Map<Long, List<MaterialUsageDto>> materialsByTech = materialData.stream()
                    .collect(Collectors.groupingBy(
                            TechnicianMaterialProjection::getTechnicianId,
                            Collectors.mapping(m -> new MaterialUsageDto(
                                    m.getProductName(),
                                    m.getTotalQuantity(),
                                    m.getUnit()
                            ), Collectors.toList())
                    ));
            log.debug("Grouped materials by technician: {}", materialsByTech);

            List<TechnicianLeaderboardDto> result = leaderboardData.stream()
                    .map(t -> {
                        List<MaterialUsageDto> materials = materialsByTech.getOrDefault(t.getTechnicianId(), Collections.emptyList());
                        log.debug("Mapping technicianId={} with {} materials", t.getTechnicianId(), materials.size());
                        return new TechnicianLeaderboardDto(
                                t.getTechnicianId(),
                                t.getTechnicianName(),
                                t.getCompletedTasks(),
                                t.getAvgRating(),
                                t.getRank(),
                                materials
                        );
                    })
                    .collect(Collectors.toList());

            log.info("Final leaderboard DTO list size: {}", result.size());
            return result;

        } catch (DateTimeParseException e) {
            log.error("Invalid date format: startDate={} endDate={}", startDate, endDate, e);
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd", e);
        } catch (Exception e) {
            log.error("Error fetching technician leaderboard", e);
            throw new GlobalMessageExceptionHandler(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @Transactional
    public void updateTaskStatusTOInProgress(Long taskId, MultipartFile[] selfie) {
        log.info("Updating status of task with ID: {}", taskId);

        validateTaskById(taskId);

        Task task = taskRepository.findById(taskId).get();

        if (selfie == null || selfie.length == 0) {
            throw new BadRequestException("Selfie file is required to update task status.");
        }

        List<FileUploadResponse> fileUploadResponses = s3StorageService.uploadFile(selfie, "task/selfie");
        List<TaskDocuments> selfies = new ArrayList<>();

        for(FileUploadResponse fileUploadResponse : fileUploadResponses){
            TaskDocuments taskDocuments = new TaskDocuments();
            taskDocuments.setTask(task);
            taskDocuments.setDocumentUrl(fileUploadResponse.getS3Key());
            taskDocuments.setDocumentName(fileUploadResponse.getFileName());
            taskDocuments.setDocumentType(FileUploadConstants.SELFIE);
            selfies.add(taskDocuments);
        }

        taskDocumentsRepository.deleteAllByTask_TaskIdAndDocumentType(taskId, FileUploadConstants.SELFIE);
        taskDocumentsRepository.saveAll(selfies);

        int rowsUpdated = taskRepository.updateTaskStatus(taskId, TaskStatus.IN_PROGRESS);

        if (rowsUpdated > 0) {
            log.info("Successfully updated status of task with ID: {}", taskId);
        } else {
            log.info("No task found with ID: {}. Status not updated.", taskId);
        }
    }


    private Task validateTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNoFoundException("Task not found with this  task id : " + taskId));
    }

    @Override
    public void updateTaskStatusToCompleted(Long taskId) {

        log.info("Updating status of task with ID: {} to COMPLETED", taskId);

        validateTaskById(taskId);

        int rowsUpdated = taskRepository.updateTaskStatus(taskId, TaskStatus.COMPLETED);

        if (rowsUpdated > 0) {
            log.info("Successfully updated status of task with ID: {} to COMPLETED", taskId);
        } else {
            log.warn("No task found with ID: {}. Status not updated.", taskId);
        }


    }


    @Transactional
    public String submitCompletionDetails(Long taskId,
                                                  List<MaterialDtoResponse> materialDtoResponses) {

        log.info("Starting submitCompletionDetails for taskId: {}", taskId);

        if (materialDtoResponses == null
                || materialDtoResponses.isEmpty()) {
            throw new BadRequestException(
                    "No task materials provided for taskId: " + taskId
            );
        }

        List<TaskMaterial> existingMaterials = taskMaterialRepository.findByTaskId(taskId);

        saveTaskMaterials(taskId, materialDtoResponses, existingMaterials);

        updateInventoryAccordingToTask(materialDtoResponses);

        updateTaskScheduleForCompletion(taskId);
        updateTaskStatusToCompleted(taskId);

        return "Inventory Updated !!";
    }

    private void updateInventoryAccordingToTask(List<MaterialDtoResponse> taskMaterialList) {
        for (MaterialDtoResponse materialDTO : taskMaterialList) {
            long id = materialDTO.getMaterialId();

            InventoryV2 inventoryV2 = inventoryRepositoryV2.findById(id)
                    .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found !!"));

            if (materialDTO.getMaterialUnit().equalsIgnoreCase("gram")) {
                Double val = materialDTO.getMaterialQuantity() / 1000;
                inventoryV2.setStockQuantity( inventoryV2.getStockQuantity() - val );
            } else if (materialDTO.getMaterialUnit().equalsIgnoreCase("milliliter")) {
                Double val = materialDTO.getMaterialQuantity() / 1000;
                inventoryV2.setStockQuantity( inventoryV2.getStockQuantity() - val );
            } else if (materialDTO.getMaterialUnit().equalsIgnoreCase("can")
                    || materialDTO.getMaterialUnit().equalsIgnoreCase("box")) {
                Double val = materialDTO.getMaterialQuantity();
                inventoryV2.setStockQuantity( inventoryV2.getStockQuantity() - val );
            }

            inventoryRepositoryV2.save(inventoryV2);
        }
    }

    @Transactional
    public void uploadCompletionImages(Long taskId,
                                       MultipartFile[] beforeImages,
                                       MultipartFile[] afterImages) {

        log.info("Uploading completion images for taskId: {}", taskId);

        if ((beforeImages == null || beforeImages.length == 0)
                && (afterImages == null || afterImages.length == 0)) {
            throw new BadRequestException("Both before and after images are required.");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task Not Found !!"));

        List<FileUploadResponse> beforeImagesList = s3StorageService.uploadFile(beforeImages, "task/before");
        List<FileUploadResponse> afterImagesList = s3StorageService.uploadFile(afterImages, "task/after");

        taskDocumentsRepository.deleteAllByTask_TaskIdAndDocumentType(taskId, FileUploadConstants.AFTER_SERVICE);
        taskDocumentsRepository.deleteAllByTask_TaskIdAndDocumentType(taskId, FileUploadConstants.BEFORE_SERVICE);

        List<TaskDocuments> taskDocumentsList = new ArrayList<>();
        for(FileUploadResponse fileUploadResponse : beforeImagesList){
            TaskDocuments taskDocuments = new TaskDocuments();
            taskDocuments.setTask(task);
            taskDocuments.setDocumentUrl(fileUploadResponse.getS3Key());
            taskDocuments.setDocumentName(fileUploadResponse.getFileName());
            taskDocuments.setDocumentType(FileUploadConstants.BEFORE_SERVICE);
            taskDocumentsList.add(taskDocuments);
        }
        for(FileUploadResponse fileUploadResponse : afterImagesList){
            TaskDocuments taskDocuments = new TaskDocuments();
            taskDocuments.setTask(task);
            taskDocuments.setDocumentUrl(fileUploadResponse.getS3Key());
            taskDocuments.setDocumentName(fileUploadResponse.getFileName());
            taskDocuments.setDocumentType(FileUploadConstants.AFTER_SERVICE);
            taskDocumentsList.add(taskDocuments);
        }
        taskDocumentsRepository.saveAll(taskDocumentsList);
    }


    private void updateTaskScheduleForCompletion(Long taskId) {
        TaskSchedule taskSchedule = taskScheduleRepository.findByTaskId(taskId);
        if (Objects.nonNull(taskSchedule)) {
            taskSchedule.setTaskEndTime(LocalTime.now());
            taskScheduleRepository.save(taskSchedule);
        }
    }


    private void saveFeedbackList(FeedbackRequest feedbackRequest, Long taskId) {
        log.info("Into [saveFeedbackList] :: taskId = {}", taskId);

        if (Objects.isNull(feedbackRequest)) {
            log.warn("FeedbackRequest is null for taskId = {}", taskId);
            return;
        }

        try {
            Optional<Feedback> existingFeedbackOpt = feedbackRepository.findByTaskId(taskId);

            Feedback feedback;
            if (existingFeedbackOpt.isPresent()) {
                feedback = existingFeedbackOpt.get();
                log.info("Existing feedback found for taskId = {}, updating entry", taskId);
            } else {
                feedback = new Feedback();
                feedback.setTaskId(taskId);
                feedback.setCreatedAt(LocalDateTime.now());
                log.info("No feedback found for taskId = {}, creating new entry", taskId);
            }

            feedback.setComment(feedbackRequest.getComment());
            feedback.setRating(feedbackRequest.getRating());
            feedback.setActive(Boolean.TRUE);
            feedback.setUpdatedAt(LocalDateTime.now());

            feedback = feedbackRepository.save(feedback);


            updateTechnitianFeedBack(taskId, feedback.getId());
            log.info("Feedback saved successfully for taskId = {}", taskId);

        } catch (Exception e) {
            log.error("Error while saving feedback for taskId = {}", taskId, e);
        }
    }


    private void saveTaskMaterials(Long taskId, List<MaterialDtoResponse> taskMaterialDTOs, List<TaskMaterial> existingMaterials) {
        Map<Long, TaskMaterial> existingMap = new HashMap<>();
        for (TaskMaterial tm : existingMaterials) {
            existingMap.put(tm.getMaterialId(), tm);
            log.debug("Existing material mapped: materialId={}, unit={}, quantity={}, isUsed={}",
                    tm.getMaterialId(), tm.getUnit(), tm.getQuantity(), tm.getIsUsed());
        }

        List<TaskMaterial> materialsToSave = new ArrayList<>();
        for (MaterialDtoResponse dto : taskMaterialDTOs) {
            log.debug("Processing DTO: materialId={}, unit={}, quantity={}, isUsed={}",
                    dto.getMaterialId(), dto.getMaterialUnit(), dto.getMaterialQuantity(), true);
            TaskMaterial taskMaterial = null;
            if (dto.getMaterialId() != null) {
                taskMaterial = existingMap.get(dto.getMaterialId());
            }

            if (taskMaterial != null) {
                log.info("Updating existing material: materialId={}", dto.getMaterialId());
                taskMaterial.setUnit(dto.getMaterialUnit());
                taskMaterial.setIsUsed(true);
                taskMaterial.setQuantity(dto.getMaterialQuantity());
                materialsToSave.add(taskMaterial);
            } else {
                log.info("Adding new material: materialId={}", dto.getMaterialId());
                TaskMaterial newMaterial = TaskMaterial.builder()
                        .taskId(taskId)
                        .materialId(dto.getMaterialId())
                        .unit(dto.getMaterialUnit())
                        .isUsed(true)
                        .quantity(dto.getMaterialQuantity())
                        .build();
                materialsToSave.add(newMaterial);
            }
        }

        log.info("Saving {} task materials for taskId: {}", materialsToSave.size(), taskId);
        taskMaterialRepository.saveAll(materialsToSave);
        log.info("Task materials successfully updated for taskId: {}", taskId);
    }


    @Override
    public List<TechnicianTaskMapper> getTechnitianByTaskId(long taskId) {
        List<TechnicianTaskMapper> technicianTaskMappers =
                technicianTaskMapperRepository.getTechnitiansByTaskId(taskId);

        if (Objects.isNull(technicianTaskMappers) || technicianTaskMappers.isEmpty()) {
            throw new RuntimeException("Technitian details not found by taskId");
        }

        return technicianTaskMappers;
    }

    @Override
    public void updateTechnitianFeedBack(long taskId, long feedbackId) {

        Integer noOfRecordsUpdated =
                technicianTaskMapperRepository.updateTechnitianFeedBackDetails(taskId, feedbackId);

        if (noOfRecordsUpdated == 0) {
            throw new DBReltedException("Error While updating feedback details into technitian please retry");
        }

    }

    @Override
    public List<TechnitianFeedbackDetailProjection> getTechnitianFeedbackDetails(long feedbackId) {

        return technicianTaskMapperRepository.getTechnitianFeedbackDetails(feedbackId);
    }

    @Override
    public ResultDto<TechnicianResponseDTO> searchTasks(FilterRequest filterRequest) {

        ResultDto<TechnicianResponseDTO> resultDto =
                taskTechnicianCustomRepository.searchTasks(filterRequest);

        List<TechnicianResponseDTO> tasks = resultDto.getResults();

        for (int i = 0; i < tasks.size(); i++) {
            TechnicianResponseDTO task = tasks.get(i);
            task.setSalfie(toRespectiveUrl(taskDocumentsRepository.findAllByTaskIdAndDocumentType(task.getTaskId(), FileUploadConstants.SELFIE)));
            task.setAfterImagerUrl(toRespectiveUrl(taskDocumentsRepository.findAllByTaskIdAndDocumentType(task.getTaskId(), FileUploadConstants.AFTER_SERVICE)));
            task.setBeforeImageUrl(toRespectiveUrl(taskDocumentsRepository.findAllByTaskIdAndDocumentType(task.getTaskId(), FileUploadConstants.BEFORE_SERVICE)));
        }


        return resultDto;
    }

    private List<String> toRespectiveUrl(List<String> files) {
        List<String> imageUrls = files.stream()
                .map(this::generatePresignedUrl)
                .toList();

        return imageUrls;
    }

    private String generatePresignedUrl(String s3Key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(p -> p
                        .getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10)));

        return presignedRequest.url().toString();
    }

    @Override
    public ResultDto<TechnicianResponseDTO> searchTasks() {
        ResultDto<TechnicianResponseDTO> resultDto =
                taskTechnicianCustomRepository.searchTasks();

        List<TechnicianResponseDTO> tasks = resultDto.getResults();

        for (int i = 0; i < tasks.size(); i++) {
            TechnicianResponseDTO task = tasks.get(i);

            task.setSalfie(toRespectiveUrl(taskDocumentsRepository.findAllByTaskIdAndDocumentType(task.getTaskId(), FileUploadConstants.SELFIE)));
            task.setAfterImagerUrl(toRespectiveUrl(taskDocumentsRepository.findAllByTaskIdAndDocumentType(task.getTaskId(), FileUploadConstants.AFTER_SERVICE)));
            task.setBeforeImageUrl(toRespectiveUrl(taskDocumentsRepository.findAllByTaskIdAndDocumentType(task.getTaskId(), FileUploadConstants.BEFORE_SERVICE)));
        }

        return resultDto;
    }


    private List<MaterialDtoResponse> getTaskMaterial(Long taskId) {

        List<TaskMaterial> taskMaterialList = taskMaterialRepository.findByTaskId(taskId);
        List<MaterialDtoResponse> list = new ArrayList<>();

        for (TaskMaterial task : taskMaterialList) {

            Inventory item = inventoryRepository.findByItemId(task.getMaterialId());
            if (item == null) continue;

            MaterialDtoResponse dto = new MaterialDtoResponse(); // ✅ NEW object
            dto.setMaterialId(item.getItemId());
            dto.setMaterialName(item.getItemName());
            dto.setMaterialUnit(task.getUnit());
            dto.setMaterialQuantity(task.getQuantity());

            list.add(dto);
        }
        return list;
    }

    @Override
    public OtpResponseDTO feedbackSubmission(FeedbackRequest feedbackRequest, Long taskId) {
        OtpResponseDTO otpResponseDTO =
                otpService.validateOtp(
                        feedbackRequest.getMobileNo(),
                        feedbackRequest.getOtp()
                );

        saveFeedbackList(feedbackRequest, taskId);

        return otpResponseDTO;
    }
}




