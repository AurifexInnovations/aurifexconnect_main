package com.erp.Service.TaskService;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.*;
import com.erp.Enum.TaskStatus;
import com.erp.Exception.Task.TaskNoFoundException;
import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Mapper.TaskMapper.TaskDetailsMapper;
import com.erp.Mapper.TaskMapper.TaskMapper;

import com.erp.Model.Task;

import com.erp.Model.*;
import com.erp.Projection.LeaderboardProjection;
import com.erp.Projection.TechnicianPerformanceProjection;
import com.erp.Projection.TechnicianResponse;
import com.erp.Projection.TechnicianTaskProjection;
import com.erp.Projection.TechnitianFeedbackDetailProjection;
import com.erp.Repository.Task.*;
import com.erp.Service.ServiceType.ServiceType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    private final ServiceType serviceType;

    @Override
    @Transactional
    public TaskResponse addTask(TaskRequest taskRequest) {
        log.info("Into [TaskServiceImpl]  [addTask] Into add task ");

        Task task;

        try {

            if (Objects.isNull(taskRequest)) {
                return null;
            }

            if (taskRequest.getTaskId() != null) {

                Task existingTax = taskRepository.findById(taskRequest.getTaskId())
                        .orElseThrow(() -> new TaxNotFoundException("Tax not found with Id: " + taskRequest.getTaskId()));

                taskMapper.mapToTaxEntity(taskRequest, existingTax);
            }


            task = taskMapper.mapToTask(taskRequest);


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


    private void addTaskSchedule(TaskRequest taskRequest) {

        log.info("Into add  TaskSchedule...");

        Optional<TaskSchedule> optionalTaskSchedule =
                taskScheduleRepository.findById(taskRequest.getTaskId());

        TaskSchedule taskSchedule = optionalTaskSchedule.orElseGet(TaskSchedule::new);

        taskSchedule = taskDetailsMapper.mapToTaskSchedule(taskRequest, taskSchedule);

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
    public boolean getTask(long taskId) {

        return taskRepository.findById(taskId).isPresent();
    }


    @Override
    public List<TechnicianResponse> getTechnicians(TechnicianRequest technicianRequest) {
        log.info("[TaskService] [getTechnicians] Entered with request: {}", technicianRequest);

        List<TechnicianResponse> technicianList = new ArrayList<>();

        try {

            if (technicianRequest.getStatus() != null
                    && technicianRequest.getCategory() != null
                    && technicianRequest.getStartDate() != null
                    && technicianRequest.getEndDate() != null) {

                Pageable pageable = PageRequest.of(
                        technicianRequest.getOffset() / technicianRequest.getSize(), // page number
                        technicianRequest.getSize(),
                        Sort.by(Sort.Direction.DESC, "task_id")
                );

//                LocalDate startDate = technicianRequest.getStartDate();
//                LocalDate endDate = technicianRequest.getEndDate();
//
//                if (startDate == null) startDate = LocalDate.of(startDate);
//                if (endDate == null) endDate = LocalDate.of(endDate);

                LocalDate startDate = technicianRequest.getStartDate();
                LocalDate endDate = technicianRequest.getEndDate();
                String  status = technicianRequest.getStatus();
                Boolean isActive = Boolean.FALSE;
                if(Objects.nonNull(status) && status.equals("active")){
                    isActive= Boolean.TRUE;
                }

                  technicianList =  taskRepository.searchTasksWithScheduleAndTechnicians(
                        startDate,
                        endDate,
                        isActive,
                        technicianRequest.getCategory()
                );

            }
            log.info("[TaskService] [getTechnicians] Found {} technicians", technicianList.size());
        } catch (Exception e) {
            log.error("[TaskService] [getTechnicians] Error while fetching technicians", e);
            throw new RuntimeException("Failed to fetch technicians", e);
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




    public TechnicianPerformanceResponse getTechnicianPerformance(LocalDate startDate, LocalDate endDate) {
        log.info("Starting getTechniciansReportPerformanceByAssigenDate with startDate={} and endDate={}", startDate, endDate);

        List<TechnicianPerformanceProjection> performanceList = taskScheduleRepository.getTechnicianPerformance(startDate, endDate);
        List<LeaderboardProjection> leaderboardList = taskScheduleRepository.getLeaderboard();

        Map<Long, TechnicianStatsDTO> technicianStatsMap = new HashMap<>();
        Map<Long, List<ChemicalUsageDTO>> chemicalUsageMap = new HashMap<>();

        for (TechnicianPerformanceProjection p : performanceList) {

            technicianStatsMap.computeIfAbsent(p.getTechnicianId(), id -> {
                TechnicianStatsDTO stats = new TechnicianStatsDTO();
                stats.setTechnicianId(id);
                stats.setTasksCompleted(p.getTasksCompleted());
                stats.setAverageRating(p.getAverageRating());
                return stats;
            });

            if (p.getProductName() != null) {
                chemicalUsageMap.computeIfAbsent(p.getTechnicianId(), id -> new ArrayList<>())
                        .add(new ChemicalUsageDTO(p.getProductName(), p.getQuantity(), p.getUnit()));
            }
        }

        List<TechnicianStatsDTO> technicianStats = technicianStatsMap.values().stream().map(stats -> {
            stats.setChemicalUsage(chemicalUsageMap.getOrDefault(stats.getTechnicianId(), new ArrayList<>()));
            return stats;
        }).collect(Collectors.toList());

        List<LeaderboardDTO> leaderboard = leaderboardList.stream()
                .map(l -> new LeaderboardDTO(
                        l.getTechnicianId(),
                        l.getName(),
                        l.getRank(),
                        l.getAverageRating()
                ))
                .collect(Collectors.toList());

        return new TechnicianPerformanceResponse(technicianStats, leaderboard);
    }




    @Override
    public void updateTaskStatusTOInProgress(Long taskId) {
        log.info("Updating status of task with ID: {}", taskId);

        validateTaskById(taskId);

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

        Task task = validateTaskById(taskId);

        int rowsUpdated = taskRepository.updateTaskStatus(taskId, TaskStatus.COMPLETED);

        if (rowsUpdated > 0) {
            log.info("Successfully updated status of task with ID: {} to COMPLETED", taskId);
        } else {
            log.warn("No task found with ID: {}. Status not updated.", taskId);
        }


    }


    @Transactional
    public void updateTaskMaterialForStatusProgress(Long taskId, List<TaskMaterialDTO> taskMaterialList) {
        log.info("Starting updateTaskMaterialForStatusProgress for taskId: {}", taskId);

        if (taskMaterialList == null || taskMaterialList.isEmpty()) {
            log.warn("No task materials provided for taskId: {}", taskId);
            return;
        }

        try {
            log.info("Fetching existing task materials for taskId: {}", taskId);
            List<TaskMaterial> existingMaterials = taskMaterialRepository.findByTaskId(taskId);
            log.info("Found {} existing task materials for taskId: {}", existingMaterials.size(), taskId);

            Map<Long, TaskMaterial> existingMap = new HashMap<>();
            for (TaskMaterial tm : existingMaterials) {
                existingMap.put(tm.getMaterialId(), tm);
                log.debug("Existing material mapped: materialId={}, unit={}, quantity={}, isUsed={}",
                        tm.getMaterialId(), tm.getUnit(), tm.getQuantity(), tm.getIsUsed());
            }

            List<TaskMaterial> materialsToSave = new ArrayList<>();

            for (TaskMaterialDTO dto : taskMaterialList) {
                log.debug("Processing DTO: materialId={}, unit={}, quantity={}, isUsed={}",
                        dto.getMaterialId(), dto.getUnit(), dto.getQuantity(), dto.getIsUsed());

                TaskMaterial taskMaterial = existingMap.get(dto.getMaterialId());

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

        } catch (Exception e) {
            log.error("Error updating task materials for taskId: {}", taskId, e);
            throw e;
        }
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
            throw new RuntimeException("Error While updating feedback details into technitian please retry");
        }

    }

    @Override
    public List<TechnitianFeedbackDetailProjection> getTechnitianFeedbackDetails(long feedbackId) {
        List<TechnitianFeedbackDetailProjection> technitianFeedbackDetailProjections =
                technicianTaskMapperRepository.getTechnitianFeedbackDetails(feedbackId);

        return technitianFeedbackDetailProjections;
    }
}


