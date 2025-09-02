package com.erp.TechnicianApp.TechnicianService.Task;

import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianTaskRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.TaskStatusUpdateRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianTaskResponse;

import java.util.List;

public interface TechnicianTaskService {

    TechnicianTaskResponse createTask(TechnicianTaskRequest request);

    TechnicianTaskResponse assignTaskToTechnician(Long taskId, Long technicianId);

    List<TechnicianTaskResponse> getTasksByTechnicianId(Long technicianId);

    TechnicianTaskResponse updateTaskStatus(Long taskId, TaskStatusUpdateRequest request);

    TechnicianTaskResponse updateTaskDetails(Long taskId, TechnicianTaskRequest request);

    List<TechnicianTaskResponse> getAllTasks();

    TechnicianTaskResponse getTaskById(Long taskId);

    void deleteTask(Long taskId);
}
