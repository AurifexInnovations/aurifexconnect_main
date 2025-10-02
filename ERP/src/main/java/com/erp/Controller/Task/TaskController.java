package com.erp.Controller.Task;


import com.erp.Dto.Request.TaskRequest;
import com.erp.Dto.Response.TaskResponse;
import com.erp.Service.TaskService.TaskService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
@Tag(name = "Task Controller", description = "API Endpoints for Managing Task Data")
public class TaskController {

    private final TaskService taskService;


    @PostMapping("task")
    @Operation(description = "Create a New Task Entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task Created Successfully"),
            })
    public ResponseEntity<ResponseStructure<TaskResponse>> addTax(@Valid @RequestBody TaskRequest taskRequest) {
        log.info("[TaskController]  [addTax]  into adding task  {}",taskRequest);
        TaskResponse response = taskService.addTask(taskRequest);
        log.info("[TaskController]  [addTax]  exit from  adding task  {}",response);
        return ResponseBuilder.success(HttpStatus.CREATED, "Task Created", response);

    }

}
