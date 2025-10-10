package com.erp.Controller.Task;


import com.erp.Dto.Request.*;
import com.erp.Dto.Response.*;
import com.erp.Projection.TechnicianResponse;
import com.erp.Projection.TechnicianTaskProjection;
import com.erp.Service.TaskService.TaskService;
import com.erp.Utility.ListResponseStructure;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
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


    @GetMapping("/tasks")
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




    @PostMapping("/technician/search")
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





    @PostMapping("/tracking")
    @Operation(
            summary = "Search Technicians by Date and Assigned Date",
            description = "Retrieve all technicians based on date and assigned date criteria.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Technicians retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<List<TechnicianTaskProjection>> searchTechniciansByDateAndAssignedDate(
            @Valid @RequestBody TechnicianTaskRequest technicianTaskRequest) {

        log.info("[TechnicianController] Entering searchTechniciansByDateAndAssignedDate with request: {}", technicianTaskRequest);

        List<TechnicianTaskProjection> technicians;
        try {
            technicians = taskService.getTechniciansByDateAndAssigenDate(technicianTaskRequest);
            log.info("[TechnicianController] Found {} technicians matching date criteria", technicians.size());
        } catch (Exception e) {
            log.error("[TechnicianController] Error while searching technicians by date and assigned date", e);
            return ResponseEntity.status(500).build();
        }

        log.info("[TechnicianController] Returning response successfully");
        return ResponseEntity.ok(technicians);
    }



    @GetMapping("/task/performance/report")
    public ResponseEntity<ListResponseStructure<TechnicianLeaderboardDto>> getTechnicianPerformanceReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        log.info("[TechnicianPerformanceController] Entering getTechnicianPerformanceReport" +
                " with startDate: {} and endDate: {}", startDate, endDate);

        List<TechnicianLeaderboardDto> technicianLeaderboardDto = taskService.getTechnicianLeaderboard(startDate, endDate);

        log.info("[TechnicianPerformanceController] Returning response with {} technicians", technicianLeaderboardDto.size());

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Technician performance report retrieved successfully",
                technicianLeaderboardDto
        );
    }





    @PostMapping("/task/start/{taskId}")
    @Operation(
            summary = "Update Task Status to IN_PROGRESS",
            description = "Update the status of a task to IN_PROGRESS by task ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task status updated to IN_PROGRESS"),
                    @ApiResponse(responseCode = "400", description = "Invalid task ID")
            }
    )
    public ResponseEntity<ResponseStructure<String>> updateTaskStatusTOInProgress(@PathVariable Long taskId,
                                                                            @RequestParam("files") MultipartFile[] selfie) {
        taskService.updateTaskStatusTOInProgress(taskId,selfie);
        return ResponseBuilder.success(HttpStatus.OK, "Task status updated to IN_PROGRESS", "Task ID: " + taskId);
    }



    @PostMapping("/task/completed/{taskId}")
    @Operation(
            summary = "Update Task Materials for a Task",
            description = "Update or add task materials for the given task ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task materials updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid task ID or task material list")
            }
    )
    public ResponseEntity<ResponseStructure<OtpResponseDTO>> updateTaskMaterials(@PathVariable ("taskId") Long taskId,
            @RequestPart("completeTaskRequestDTO") CompleteTaskRequestDTO completeTaskRequestDTO,
            @RequestParam("beforeImages") MultipartFile[] beforeImages,
            @RequestParam("afterImages") MultipartFile[] afterImages) {

        OtpResponseDTO otpResponseDTO = taskService.updateTaskMaterialForStatusProgress(taskId,completeTaskRequestDTO,beforeImages,afterImages);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Task Completed  successfully",
                otpResponseDTO
        );
    }




}
