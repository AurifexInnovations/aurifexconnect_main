package com.erp.Service.TaskService;

import com.erp.Dto.Request.TaskRequest;
import com.erp.Dto.Response.GetAllTaskResponse;
import com.erp.Dto.Response.TaskResponse;

import java.util.List;

public interface TaskService {

    TaskResponse addTask(TaskRequest taskRequest);

    List<GetAllTaskResponse> getAllTasks(Integer page, Integer size);




}
