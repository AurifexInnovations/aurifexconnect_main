package com.erp.Controller.TaskActivity;

import com.erp.Dto.Request.TaskActivityRequest;
import com.erp.Dto.Response.TaskActivityResponse;
import com.erp.Service.TaskActivity.TaskActivityService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/crm/tasks")
public class TaskActivityController {

    private final TaskActivityService taskService;

    @PostMapping("/create")
    @Operation(summary = "Create Task/Activity", description = "Create a new task/activity linked to leads, contacts, or deals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully")
    })
    public ResponseEntity<ResponseStructure<TaskActivityResponse>> create(
            @Valid @RequestBody TaskActivityRequest request) {
        TaskActivityResponse response = taskService.create(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Task created successfully", response);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Task/Activity", description = "Update details of an existing task/activity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<ResponseStructure<TaskActivityResponse>> update(
            @PathVariable Long id, @Valid @RequestBody TaskActivityRequest request) {
        TaskActivityResponse response = taskService.update(id, request);
        return ResponseBuilder.success(HttpStatus.OK, "Task updated successfully", response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Task by ID", description = "Fetch task/activity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<ResponseStructure<TaskActivityResponse>> getById(@PathVariable Long id) {
        TaskActivityResponse response = taskService.getById(id);
        return ResponseBuilder.success(HttpStatus.OK, "Task found", response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get All Tasks/Activities", description = "Fetch all tasks/activities")
    public ResponseEntity<ListResponseStructure<TaskActivityResponse>> getAll() {
        List<TaskActivityResponse> responses = taskService.getAll();
        return ResponseBuilder.success(HttpStatus.OK, "All tasks fetched", responses);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get Tasks by Status", description = "Fetch tasks filtered by status")
    public ResponseEntity<ListResponseStructure<TaskActivityResponse>> getByStatus(@PathVariable String status) {
        List<TaskActivityResponse> responses = taskService.getByStatus(status);
        return ResponseBuilder.success(HttpStatus.OK, "Tasks by status fetched", responses);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get Tasks by Type", description = "Fetch tasks filtered by type")
    public ResponseEntity<ListResponseStructure<TaskActivityResponse>> getByType(@PathVariable String type) {
        List<TaskActivityResponse> responses = taskService.getByType(type);
        return ResponseBuilder.success(HttpStatus.OK, "Tasks by type fetched", responses);
    }

    @GetMapping("/due-date/{date}")
    @Operation(summary = "Get Tasks by Due Date", description = "Fetch tasks by due date")
    public ResponseEntity<ListResponseStructure<TaskActivityResponse>> getByDueDate(@PathVariable LocalDate date) {
        List<TaskActivityResponse> responses = taskService.getByDueDate(date);
        return ResponseBuilder.success(HttpStatus.OK, "Tasks by due date fetched", responses);
    }

    @GetMapping("/lead/{leadId}")
    @Operation(summary = "Get Tasks for Lead", description = "Fetch tasks linked to a lead")
    public ResponseEntity<ListResponseStructure<TaskActivityResponse>> getByRelatedLead(@PathVariable Long leadId) {
        List<TaskActivityResponse> responses = taskService.getByRelatedLead(leadId);
        return ResponseBuilder.success(HttpStatus.OK, "Tasks for lead fetched", responses);
    }

    @GetMapping("/contact/{contactId}")
    @Operation(summary = "Get Tasks for Contact", description = "Fetch tasks linked to a contact")
    public ResponseEntity<ListResponseStructure<TaskActivityResponse>> getByRelatedContact(@PathVariable Long contactId) {
        List<TaskActivityResponse> responses = taskService.getByRelatedContact(contactId);
        return ResponseBuilder.success(HttpStatus.OK, "Tasks for contact fetched", responses);
    }

    @GetMapping("/deal/{dealId}")
    @Operation(summary = "Get Tasks for Deal", description = "Fetch tasks linked to a deal")
    public ResponseEntity<ListResponseStructure<TaskActivityResponse>> getByRelatedDeal(@PathVariable Long dealId) {
        List<TaskActivityResponse> responses = taskService.getByRelatedDeal(dealId);
        return ResponseBuilder.success(HttpStatus.OK, "Tasks for deal fetched", responses);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Task", description = "Delete task by ID")
    public ResponseEntity<ResponseStructure<String>> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseBuilder.success(HttpStatus.OK, "Task deleted successfully", "Deleted successfully");
    }
}