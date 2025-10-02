package com.erp.Service.TaskService;

import com.erp.Dto.Request.TaskRequest;
import com.erp.Dto.Response.GetAllTaskResponse;
import com.erp.Dto.Response.TaskResponse;
import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Mapper.TaskMapper.TaskMapper;

import com.erp.Model.Task;
import com.erp.Model.Tax;
import com.erp.Repository.Task.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @Override
    public TaskResponse addTask(TaskRequest taskRequest) {
     log.info("");

     try {

         if(Objects.isNull(taskRequest)){
             return  null;
         }

         if(taskRequest.getTaskId() !=null){

             Task existingTax = taskRepository.findById(taskRequest.getTaskId())
                     .orElseThrow(() -> new TaxNotFoundException("Tax not found with Id: " + taskRequest.getTaskId()));

             taskMapper.mapToTaxEntity(taskRequest, existingTax);
         }

         Task  task =  taskMapper.mapToTask(taskRequest);


        task =  taskRepository.save(task);



     } catch (Exception e) {
         throw new RuntimeException(e);
     }


        return null;
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





}
