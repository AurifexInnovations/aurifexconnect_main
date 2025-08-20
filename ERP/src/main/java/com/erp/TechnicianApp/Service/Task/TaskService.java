package com.erp.TechnicianApp.Service.Task;


import com.erp.TechnicianApp.Dto.Request.CommonParam;
import com.erp.TechnicianApp.Dto.Request.TaskRequest;
import com.erp.TechnicianApp.Dto.Request.TaskStatusUpdateRequest;
import com.erp.TechnicianApp.Dto.Response.TaskResponse;

import java.util.List;

public interface TaskService {

    TaskResponse addTask(TaskRequest taskRequest);

    TaskResponse assignTaskToTechnician(Long taskId, Long technicianId);

    List<TaskResponse> getTasksByTechnicianId(Long technicianId);

    TaskResponse updateTaskStatus(Long taskId, TaskStatusUpdateRequest request);

    TaskResponse updateTaskDetails(Long taskId, TaskRequest taskRequest);

    List<TaskResponse> getAllTask();

    List<TaskResponse> findById(CommonParam param);
}
