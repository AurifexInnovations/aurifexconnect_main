package com.erp.TechnicianApp.TechnicianController.Task;

import com.erp.Dto.Request.Param;
import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianTaskRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.TaskStatusUpdateRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianTaskResponse;
import com.erp.TechnicianApp.TechnicianService.Task.TechnicianTaskService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/technician/task")
public class TechnicianTaskController {

    private final TechnicianTaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<TechnicianTaskResponse>> createTask(
            @Valid @RequestBody TechnicianTaskRequest request) {
        TechnicianTaskResponse response = taskService.createTask(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Technician task created successfully", response);
    }

    @PutMapping("/{taskId}/assign/{technicianId}")
    public ResponseEntity<ResponseStructure<TechnicianTaskResponse>> assignTaskToTechnician(
            @PathVariable Long taskId, @PathVariable Long technicianId) {
        TechnicianTaskResponse response = taskService.assignTaskToTechnician(taskId, technicianId);
        return ResponseBuilder.success(HttpStatus.OK, "Task assigned to technician successfully", response);
    }

    @PostMapping("/technician-tasks")
    public ResponseEntity<ListResponseStructure<TechnicianTaskResponse>> getTasksByTechnicianId(
            @Valid @RequestBody Param param) {
        List<TechnicianTaskResponse> responses = taskService.getTasksByTechnicianId(param.getId());
        return ResponseBuilder.success(HttpStatus.OK, "All tasks fetched for the technician", responses);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<ResponseStructure<TechnicianTaskResponse>> updateTaskStatus(
            @PathVariable Long taskId, @Valid @RequestBody TaskStatusUpdateRequest request) {
        TechnicianTaskResponse response = taskService.updateTaskStatus(taskId, request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician task status updated successfully", response);
    }

    @PutMapping("/{taskId}/update")
    public ResponseEntity<ResponseStructure<TechnicianTaskResponse>> updateTaskDetails(
            @PathVariable Long taskId, @Valid @RequestBody TechnicianTaskRequest request) {
        TechnicianTaskResponse response = taskService.updateTaskDetails(taskId, request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician task updated successfully", response);
    }

    @GetMapping("/all")
    public ResponseEntity<ListResponseStructure<TechnicianTaskResponse>> getAllTasks() {
        List<TechnicianTaskResponse> responses = taskService.getAllTasks();
        return ResponseBuilder.success(HttpStatus.OK, "All technician tasks fetched successfully", responses);
    }

    @PostMapping("/record")
    public ResponseEntity<ResponseStructure<TechnicianTaskResponse>> getTaskById(
            @Valid @RequestBody Param param) {
        TechnicianTaskResponse response = taskService.getTaskById(param.getId());
        return ResponseBuilder.success(HttpStatus.OK, "Technician task record found", response);
    }
}