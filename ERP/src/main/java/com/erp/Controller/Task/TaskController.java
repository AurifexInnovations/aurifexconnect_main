package com.erp.Controller.Task;


import com.erp.Dto.Request.TaskRequest;
import com.erp.Dto.Request.TechnicianRequest;
import com.erp.Dto.Response.GetAllTaskResponse;
import com.erp.Dto.Response.TaskResponse;
import com.erp.Projection.TechnicianResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getAll")
    @Operation(
            summary = "Get All Task Requests",
            description = "Retrieve all task requests with optional pagination.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task requests retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<ResponseStructure<List<GetAllTaskResponse>>> getAllTasks(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        // Set default values if null
        int safePage = (page == null || page < 0) ? 0 : page;
        int safeSize = (size == null || size <= 0) ? 10 : size;

        log.info("[TaskController] [getAllTaskRequests] Fetching tasks, page: {}, size: {}", safePage, safeSize);

        List<GetAllTaskResponse> response = taskService.getAllTasks(safePage, safeSize);

        log.info("[TaskController] [getAllTaskRequests] Fetched {} tasks", response.size());

        return ResponseBuilder.<List<GetAllTaskResponse>>success(
                HttpStatus.OK,
                "Task requests retrieved successfully",
                response
        );
    }

    @PostMapping("/search")
    @Operation(
            summary = "Search Task Technicians",
            description = "Retrieve all technicians matching search criteria.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Technicians retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<List<TechnicianResponse>> searchTechnicians(
            @Valid @RequestBody TechnicianRequest technicianRequest) {

        log.info("[TechnicianController] Entering searchTechnicians with request: {}", technicianRequest);

        List<TechnicianResponse> technicians;
        try {
            technicians = taskService.getTechnicians(technicianRequest);
            log.info("[TechnicianController] Found {} technicians matching criteria", technicians.size());
        } catch (Exception e) {
            log.error("[TechnicianController] Error while searching technicians", e);
            return ResponseEntity.status(500).build();
        }

        log.info("[TechnicianController] Returning response successfully");
        return ResponseEntity.ok(technicians);
    }





}
