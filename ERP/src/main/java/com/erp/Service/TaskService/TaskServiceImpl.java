package com.erp.Service.TaskService;

import com.erp.CustomRepository.InventoryCustomRepository;
import com.erp.CustomRepository.TaskTechnicianCustomRepository;
import com.erp.Dto.Request.*;
import com.erp.Dto.Response.*;
import com.erp.Dto.Response.TechnicianResponse;
import com.erp.Enum.TaskStatus;
import com.erp.Exception.BadRequestException;
import com.erp.Exception.DBReltedException;
import com.erp.Exception.GlobalMessageExceptionHandler;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.Task.TaskNoFoundException;

import com.erp.Mapper.TaskMapper.TaskDetailsMapper;
import com.erp.Mapper.TaskMapper.TaskMapper;

import com.erp.Model.Task;

import com.erp.Model.*;
import com.erp.Projection.*;
import com.erp.Repository.Feedback.FeedbackRepository;
import com.erp.Repository.Task.*;
import com.erp.Repository.Utility.FileRepository;
import com.erp.Security.util.UserIdentity;

import com.erp.Service.InventoryService.InventoryService;
import com.erp.Service.Otp.OtpService;
import com.erp.Service.Utility.FileService;
import com.erp.constants.FileUploadConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;


import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

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

    private  final FileRepository fileRepository;


    @Lazy
    @Autowired
    private FileService fileService;

    private final UserIdentity userIdentity;

    private final OtpService otpService;

    @Override
    @Transactional
    public TaskResponse addTask(TaskRequest taskRequest) {
        log.info("Into [TaskServiceImpl]  [addTask] Into add task ");

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

        Optional<TaskSchedule> optionalTaskSchedule =
                taskScheduleRepository.findById(taskRequest.getTaskId());

        TaskSchedule taskSchedule = optionalTaskSchedule.orElseGet(TaskSchedule::new);

        taskSchedule = taskDetailsMapper.mapToTaskSchedule(taskRequest, taskSchedule);
        taskSchedule.setTaskStartTime(LocalTime.now());
        taskScheduleRepository.save(taskSchedule);

        log.info("TaskSchedule processed successfully");


    }


    public void addServiceToTask(TaskRequest taskRequest) {

        Long taskId = taskRequest.getTaskId();
        log.info("Initiating service update for Task ID: {}", taskId);
        try {

            log.debug("Deleting existing service mappings for Task ID: {}", taskId);
            taskServiceMapperRepository.deleteByTaskId(taskId);


            log.debug("Mapping new services for Task ID: {}", taskId);
            List<TaskServiceMapper> taskServiceMappers = taskDetailsMapper.mapServicesToTask(taskRequest);


            log.debug("Saving {} service mappings for Task ID: {}", taskServiceMappers.size(), taskId);
            taskServiceMapperRepository.saveAll(taskServiceMappers);

            log.info("Service update completed successfully for Task ID: {}", taskId);
        } catch (Exception e) {
            log.error("Error occurred while updating services for Task ID: {}", taskId, e);
            throw e;
        }

    }

    public void addTechniciansToTask(TaskRequest taskRequest) {

        Long taskId = taskRequest.getTaskId();
        log.info("Initiating technician update for Task ID: {}", taskId);

        try {
            log.debug("Deleting existing technician mappings for Task ID: {}", taskId);
            technicianTaskMapperRepository.deleteByTaskId(taskId);

            log.debug("Mapping new technicians for Task ID: {}", taskId);
            List<TechnicianTaskMapper> taskTechnicianMappers = taskDetailsMapper.mapTechniciansToTask(taskRequest);

            log.debug("Saving {} technician mappings for Task ID: {}", taskTechnicianMappers.size(), taskId);
            technicianTaskMapperRepository.saveAll(taskTechnicianMappers);

            log.info("Technician update completed successfully for Task ID: {}", taskId);
        } catch (Exception e) {
            log.error("Error occurred while updating technicians for Task ID: {}", taskId, e);
            throw e;
        }
    }


    public void addMaterialsToTask(TaskRequest taskRequest) {

        Long taskId = taskRequest.getTaskId();
        log.info("Initiating material update for Task ID: {}", taskId);

        try {
            log.debug("Deleting existing material mappings for Task ID: {}", taskId);
            taskMaterialRepository.deleteByTaskId(taskId);

            log.debug("Mapping new materials for Task ID: {}", taskId);
            List<TaskMaterial> taskMaterialMappers = taskDetailsMapper.mapMaterialsToTask(taskRequest);

            log.debug("Saving {} material mappings for Task ID: {}", taskMaterialMappers.size(), taskId);
            taskMaterialRepository.saveAll(taskMaterialMappers);

            log.info("Material update completed successfully for Task ID: {}", taskId);
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
            }
            else{
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
    public void updateTaskStatusTOInProgress(Long taskId, MultipartFile[] selfie) {
        log.info("Updating status of task with ID: {}", taskId);

        validateTaskById(taskId);

        if (selfie == null || selfie.length == 0) {
            throw new BadRequestException("Selfie file is required to update task status.");
        }

        fileService.uploadFiles(taskId, FileUploadConstants.SELFIE, selfie);


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
    public OtpResponseDTO submitCompletionDetails(Long taskId,
                                                  CompleteTaskRequestDTO completeTaskRequestDTO) {

        log.info("Starting submitCompletionDetails for taskId: {}", taskId);

        if (completeTaskRequestDTO.getTaskMaterialList() == null
                || completeTaskRequestDTO.getTaskMaterialList().isEmpty()) {
            throw new BadRequestException(
                    "No task materials provided for taskId: " + taskId
            );
        }

        OtpResponseDTO otpResponseDTO =
                otpService.validateOtp(
                        completeTaskRequestDTO.getFeedbackList().getMobileNo(),
                        completeTaskRequestDTO.getFeedbackList().getOtp()
                );

        List<TaskMaterial> existingMaterials = taskMaterialRepository.findByTaskId(taskId);

        saveFeedbackList(completeTaskRequestDTO.getFeedbackList(), taskId);

        saveTaskMaterials(taskId, completeTaskRequestDTO.getTaskMaterialList(), existingMaterials);

        updateTaskScheduleForCompletion(taskId);
        updateTaskStatusToCompleted(taskId);

        return otpResponseDTO;
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

        fileService.uploadFiles(taskId, FileUploadConstants.BEFORE_SERVICE, beforeImages);
        fileService.uploadFiles(taskId, FileUploadConstants.AFTER_SERVICE, afterImages);
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


    private void saveTaskMaterials(Long taskId, List<TaskMaterialDTO> taskMaterialDTOs, List<TaskMaterial> existingMaterials) {
        Map<Long, TaskMaterial> existingMap = new HashMap<>();
        for (TaskMaterial tm : existingMaterials) {
            existingMap.put(tm.getMaterialId(), tm);
            log.debug("Existing material mapped: materialId={}, unit={}, quantity={}, isUsed={}",
                    tm.getMaterialId(), tm.getUnit(), tm.getQuantity(), tm.getIsUsed());
        }

        List<TaskMaterial> materialsToSave = new ArrayList<>();
        for (TaskMaterialDTO dto : taskMaterialDTOs) {
            log.debug("Processing DTO: materialId={}, unit={}, quantity={}, isUsed={}",
                    dto.getMaterialId(), dto.getUnit(), dto.getQuantity(), dto.getIsUsed());
            TaskMaterial taskMaterial = null;
            if (dto.getMaterialId() != null) {
                taskMaterial = existingMap.get(dto.getMaterialId());
            }

            if (taskMaterial != null) {
                log.info("Updating existing material: materialId={}", dto.getMaterialId());
                taskMaterial.setUnit(dto.getUnit());
                taskMaterial.setIsUsed(dto.getIsUsed());
                taskMaterial.setQuantity(dto.getQuantity());
                materialsToSave.add(taskMaterial);
            } else {
                log.info("Adding new material: materialId={}", dto.getMaterialId());
                TaskMaterial newMaterial = TaskMaterial.builder()
                        .taskId(taskId)
                        .materialId(dto.getMaterialId())
                        .unit(dto.getUnit())
                        .isUsed(dto.getIsUsed())
                        .quantity(dto.getQuantity())
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

    public ResultDto<TechnicianResponseDTO> searchTasks(FilterRequest filterRequest) {

        ResultDto<TechnicianResponseDTO> resultDto =
                taskTechnicianCustomRepository.searchTasks(filterRequest);

        List<TechnicianResponseDTO> tasks = resultDto.getResults();

        for (int i = 0; i < tasks.size(); i++) {
            TechnicianResponseDTO task = tasks.get(i);

            task.setSalfie(fileRepository.findByGenIdAndCategory(task.getTaskId(), FileUploadConstants.SELFIE));
            task.setAfterImagerUrl(fileRepository.findByGenIdAndCategory(task.getTaskId(), FileUploadConstants.AFTER_SERVICE));
            task.setBeforeImageUrl(fileRepository.findByGenIdAndCategory(task.getTaskId(), FileUploadConstants.BEFORE_SERVICE));
        }

        return resultDto;
    }


    public List<String> setImageUrlBefore(Long itemId) {

        List<FileResponse> files = fileService.getAllFiles(itemId, FileUploadConstants.BEFORE_SERVICE);
        List<String> urls = new ArrayList<String>();

        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                FileResponse f = files.get(i);
                if (f != null && f.getUrl() != null) {
                    urls.add(f.getUrl());
                }
            }
        }

        return urls;
    }

    public List<String> setImageUrAfter(Long itemId) {

        List<FileResponse> files = fileService.getAllFiles(itemId, FileUploadConstants.AFTER_SERVICE);
        List<String> urls = new ArrayList<String>();

        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                FileResponse f = files.get(i);
                if (f != null && f.getUrl() != null) {
                    urls.add(f.getUrl());
                }
            }
        }

        return urls;
    }

    public List<String> setImageSelfie(Long itemId) {

        List<String> files = fileRepository.findByGenIdAndCategory(itemId, FileUploadConstants.SELFIE);
        return files;




    }
}




