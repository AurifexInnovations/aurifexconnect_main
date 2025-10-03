package com.erp.Service.TaskService;

import com.erp.Dto.Request.TaskRequest;
import com.erp.Dto.Request.TechnicianRequest;
import com.erp.Dto.Response.GetAllTaskResponse;
import com.erp.Dto.Response.TaskResponse;
import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Mapper.TaskMapper.TaskDetailsMapper;
import com.erp.Mapper.TaskMapper.TaskMapper;

import com.erp.Model.Task;

import com.erp.Model.*;
import com.erp.Projection.TechnicianResponse;
import com.erp.Repository.Task.*;
import com.erp.Service.ServiceType.ServiceType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl  implements  TaskService{

    private  final TaskRepository   taskRepository;

    private  final TaskScheduleRepository taskScheduleRepository;

    private final TaskServiceMapperRepository taskServiceMapperRepository;

    private final TechnicianTaskMapperRepository  technicianTaskMapperRepository;

    private final TaskMaterialRepository taskMaterialRepository;

    private final TaskMapper taskMapper;

    private final TaskDetailsMapper taskDetailsMapper;

    private final ServiceType serviceType;

    @Override
    @Transactional
    public TaskResponse addTask(TaskRequest taskRequest) {
     log.info("");

        Task  task ;

        try {

         if(Objects.isNull(taskRequest)){
             return  null;
         }

         if(taskRequest.getTaskId() !=null){

             Task existingTax = taskRepository.findById(taskRequest.getTaskId())
                     .orElseThrow(() -> new TaxNotFoundException("Tax not found with Id: " + taskRequest.getTaskId()));

             taskMapper.mapToTaxEntity(taskRequest, existingTax);
         }


          task =  taskMapper.mapToTask(taskRequest);


          task =  taskRepository.save(task);
          taskRequest.setTaskId(task.getTaskId());
          addTaskSchedule(taskRequest);
          addServiceToTask(taskRequest);
          addTechniciansToTask(taskRequest);
          addMaterialsToTask(taskRequest);


     } catch (Exception e) {
         throw new RuntimeException(e);
     }

       log.info("");
        return taskDetailsMapper.mapToTaskResponse(task.getTaskId());
    }



    private  void addTaskSchedule(TaskRequest taskRequest){

        log.info("Into add  TaskSchedule...");

        Optional<TaskSchedule> optionalTaskSchedule =
                taskScheduleRepository.findById(taskRequest.getTaskId());

        TaskSchedule taskSchedule = optionalTaskSchedule.orElseGet(TaskSchedule::new);

        taskSchedule =  taskDetailsMapper.mapToTaskSchedule(taskRequest, taskSchedule);

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
    public List<TechnicianResponse> getTechnicians(TechnicianRequest technicianRequest) {
        log.info("[TaskService] [getTechnicians] Entered with request: {}", technicianRequest);

        List<TechnicianResponse> technicianList = new ArrayList<>();

        try {

            if (technicianRequest.getStatus() != null
                    && technicianRequest.getCategory() != null
                    && technicianRequest.getStartDate() != null
                    && technicianRequest.getEndDate() != null) {

                technicianList = taskRepository.searchTasksWithScheduleAndTechnicians(
                        technicianRequest.getStatus(),
                        technicianRequest.getCategory(),
                        technicianRequest.getStartDate(),
                        technicianRequest.getEndDate(),
                        technicianRequest.getOffset(),
                        technicianRequest.getSize()
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


}
