package com.erp.Controller.Task;


import com.erp.Dto.Request.*;
import com.erp.Dto.Response.*;
import com.erp.Model.Task;
import com.erp.Projection.GetAllTaskResponse;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

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

    @PutMapping("{taskId}/update-location")
    public ResponseEntity<ResponseStructure<String>> updateTaskLocation(
            @PathVariable Long taskId,
            @RequestBody TaskLocationUpdateRequest request) {

        String  updatedTask = taskService.updateTaskLocation(taskId, request);
        return ResponseBuilder.success(HttpStatus.ACCEPTED, "Location update ", updatedTask);
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

    @GetMapping("/all/tasks")
    @Operation(
            summary = "Get All Task Requests",
            description = "Retrieve all task requests with optional pagination.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task requests retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<ResponseStructure<ResultDto<GetAllTaskResponse>>> getAllTasks() {

        log.info("[TaskController] [getAllTaskRequests] Fetching tasks, page: {}, size: {}");

        ResultDto<GetAllTaskResponse> response = taskService.getAllTasks();

        log.info("[TaskController] [getAllTaskRequests] Fetched {} tasks", response.getResults().size());

        return ResponseBuilder.success(HttpStatus.OK, "All Tasks Details", response);
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

    @GetMapping("task/performance/reports")
    public ResponseEntity<ResponseStructure<ResultDto<TechnicianLeaderboardDto>>> getTechnicianPerformanceReport(@RequestBody PaginationRequest paginationRequest){
        log.info("[TechnicianPerformanceController] Entering getTechnicianPerformanceReport");

        ResultDto<TechnicianLeaderboardDto> technicianLeaderboardDtos = taskService.getTechnicianLeaderboard(paginationRequest);

        log.info("[TechnicianPerformanceController] Returning response with {} technicians", technicianLeaderboardDtos.getResults().size());

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Technician performance report retrieved successfully",
                technicianLeaderboardDtos
        );
    }


    @PostMapping("/task/start")
    @Operation(
            summary = "Update Task Status to IN_PROGRESS",
            description = "Update the status of a task to IN_PROGRESS by task ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task status updated to IN_PROGRESS"),
                    @ApiResponse(responseCode = "400", description = "Invalid task ID")
            }
    )
    public ResponseEntity<ResponseStructure<String>> updateTaskStatusTOInProgress(@RequestParam Long taskId,
                                                                            @RequestParam("files") MultipartFile[] selfie) {
        taskService.updateTaskStatusTOInProgress(taskId,selfie);
        return ResponseBuilder.success(HttpStatus.OK, "Task status updated to IN_PROGRESS", "Task ID: " + taskId);
    }



    @PostMapping("/task/completed")
    @Operation(
            summary = "Submit completion details for a task",
            description = "Submit feedback and materials for the given task ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Completion details submitted"),
                    @ApiResponse(responseCode = "400", description = "Invalid task ID or request body")
            }
    )
    public ResponseEntity<ResponseStructure<OtpResponseDTO>> submitCompletionDetails(
            @RequestParam("taskId") Long taskId,
            @RequestBody CompleteTaskRequestDTO completeTaskRequestDTO) {

        OtpResponseDTO otpResponseDTO =
                taskService.submitCompletionDetails(taskId, completeTaskRequestDTO);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Completion details submitted successfully",
                otpResponseDTO
        );
    }

    @PostMapping(value = "/task/completed/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload task completion images",
            description = "Uploads before and after images for the task",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Images uploaded successfully"),
                    @ApiResponse(responseCode = "400", description = "Images missing or invalid task ID")
            }
    )
    public ResponseEntity<ResponseStructure<String>> uploadCompletionImages(
            @RequestParam("taskId") Long taskId,
            @RequestParam("beforeImages") MultipartFile[] beforeImages,
            @RequestParam("afterImages") MultipartFile[] afterImages) {

        taskService.uploadCompletionImages(taskId, beforeImages, afterImages);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Images uploaded successfully",
                "OK"
        );
    }


    @PostMapping("/task/search")
    public ResultDto<TechnicianResponseDTO> searchTasks(@RequestBody FilterRequest filterRequest) {
        log.info("[TaskTechnicianController] /search called");
        return taskService.searchTasks(filterRequest);
    }


    @GetMapping ("/task/all")
    public ResultDto<TechnicianResponseDTO> searchTasks() {
        log.info("[TaskTechnicianController] /search called");
        return taskService.searchTasks();
    }
}
