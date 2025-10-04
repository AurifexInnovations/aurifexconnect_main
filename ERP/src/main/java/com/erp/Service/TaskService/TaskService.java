package com.erp.Service.TaskService;

import com.erp.Dto.Request.TaskRequest;
import com.erp.Dto.Request.TechnicianRequest;
import com.erp.Dto.Request.TechnicianTaskRequest;
import com.erp.Dto.Response.GetAllTaskResponse;
import com.erp.Dto.Response.TaskResponse;
import com.erp.Projection.TechnicianResponse;
import com.erp.Projection.TechnicianTaskProjection;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskResponse addTask(TaskRequest taskRequest);

    List<GetAllTaskResponse> getAllTasks(Integer page, Integer size);

    List<TechnicianResponse> getTechnicians(TechnicianRequest technicianRequest);

    List<TechnicianTaskProjection> getTechniciansByDateAndAssigenDate(TechnicianTaskRequest technicianTaskRequest);


    boolean getTask(long taskId);


}
