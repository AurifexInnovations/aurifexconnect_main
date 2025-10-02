package com.erp.Mapper.TaskMapper;
import com.erp.Dto.Request.MaterialDto;
import com.erp.Dto.Request.TaskRequest;
import com.erp.Dto.Response.TaskResponse;
import com.erp.Model.*;
import com.erp.constant.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class TaskDetailsMapper {


    public TaskResponse mapToTaskResponse(Long taskId) {

        log.info("[TaskDetailsMapper][mapToTaskResponse]  into mapToTaskResponse ...");

        TaskResponse taskResponse = new TaskResponse();

        if(taskId==null){
            return new TaskResponse();
        }

        String formattedId = String.format("T-%s-%d",
                java.time.LocalDate.now().toString().replaceAll("-", ""),
                taskId
        );

        taskResponse.setTaskId(formattedId);
        taskResponse.setMessage(AppConstants.TASK_ASSIGNED_MESSAGE);
        log.info("[TaskDetailsMapper][mapToTaskResponse]  Exit  mapToTaskResponse ...");
        return taskResponse;
    }


    public TaskSchedule mapToTaskSchedule(TaskRequest taskRequest, TaskSchedule taskSchedule){
        log.info("[TaskDetailsMapper][TaskSchedule]  into mapToTaskSchedule ...");
        if(Objects.nonNull(taskRequest)){
            taskSchedule.setTaskId(taskRequest.getTaskId());
            taskSchedule.setAssignedDate(taskRequest.getAssignedDate());
            taskSchedule.setAssignedTime(taskRequest.getAssignedTime());
            taskSchedule.setGoogleLocationLink(taskRequest.getGoogleLocationLink());
            taskSchedule.setFieldType(taskRequest.getFieldType());
            taskSchedule.setServiceLocation(taskRequest.getServiceLocation());
        }
        log.info("[TaskDetailsMapper][TaskSchedule]  Exit  mapToTaskSchedule ...");

        return taskSchedule;
    }

    public List<TaskServiceMapper> mapServicesToTask(TaskRequest taskRequest) {

        List<TaskServiceMapper> list = new ArrayList<>();

        for (Long serviceId : taskRequest.getServiceId()) {
            TaskServiceMapper taskServiceMapper = new TaskServiceMapper();
            taskServiceMapper.setTaskId(taskRequest.getTaskId());
            taskServiceMapper.setServiceId(serviceId);
            list.add(taskServiceMapper);
        }

        return list;

    }


    public List<TechnicianTaskMapper> mapTechniciansToTask(TaskRequest taskRequest) {

        List<TechnicianTaskMapper> list = new ArrayList<>();

        for (Long technicianId : taskRequest.getTechnicianId()) {
            TechnicianTaskMapper technicianTaskMapper = new TechnicianTaskMapper();
            technicianTaskMapper.setTaskId(taskRequest.getTaskId());
            technicianTaskMapper.setTechnicianId(technicianId);
            list.add(technicianTaskMapper);
        }

        return list;

    }
    public List<TaskMaterial> mapMaterialsToTask(TaskRequest taskRequest) {

        List<TaskMaterial> list = new ArrayList<>();

        for (MaterialDto materialDto : taskRequest.getMaterialDto()) {
            TaskMaterial taskMaterial = new TaskMaterial();
            taskMaterial.setTaskId(taskRequest.getTaskId());
            taskMaterial.setMaterialId(materialDto.getProductId());
            taskMaterial.setUnit(materialDto.getUnit());
            list.add(taskMaterial);
        }

        return list;
    }





}
