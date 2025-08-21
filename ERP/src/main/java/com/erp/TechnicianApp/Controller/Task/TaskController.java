package com.erp.TechnicianApp.Controller.Task;


import com.erp.TechnicianApp.Dto.Request.CommonParam;
import com.erp.TechnicianApp.Dto.Request.TaskRequest;
import com.erp.TechnicianApp.Dto.Request.TaskStatusUpdateRequest;
import com.erp.TechnicianApp.Dto.Response.TaskResponse;
import com.erp.TechnicianApp.Service.Task.TaskService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<TaskResponse>> addTask(@RequestBody TaskRequest taskRequest){
        TaskResponse taskResponse=taskService.addTask(taskRequest);
        return ResponseBuilder.success(HttpStatus.CREATED,"Task Add Successfully", taskResponse);
    }

    @PutMapping("/{taskId}/assign/{technicianId}")
    public ResponseEntity<ResponseStructure<TaskResponse>> assignTaskToTechnician(@PathVariable Long taskId, @PathVariable Long technicianId){
        TaskResponse taskResponse=taskService.assignTaskToTechnician(taskId,technicianId);
        return ResponseBuilder.success(HttpStatus.OK, "Task Assign to Technician Successfully", taskResponse);
    }

    @GetMapping("{technicianId}/tasks")
    public ResponseEntity<ListResponseStructure<TaskResponse>> getAllTechnician(@PathVariable Long technicianId){
        List<TaskResponse> taskResponses=taskService.getTasksByTechnicianId(technicianId);
        return ResponseBuilder.success(HttpStatus.OK,"All Task Found assign to particular Technician ",taskResponses);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<ResponseStructure<TaskResponse>> updateStatus(@PathVariable Long taskId, @RequestBody TaskStatusUpdateRequest request){
        TaskResponse taskResponse=taskService.updateTaskStatus(taskId, request);
        return ResponseBuilder.success(HttpStatus.OK,"Task Staus Update Successfully",taskResponse);
    }

    @PutMapping("{taskId}")
    public ResponseEntity<ResponseStructure<TaskResponse>> updateTaskDetails(@PathVariable Long taskId, @RequestBody TaskRequest taskRequest){
        TaskResponse taskResponse=taskService.updateTaskDetails(taskId,taskRequest);
        return ResponseBuilder.success(HttpStatus.OK,"Task Update Successful ", taskResponse);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ListResponseStructure<TaskResponse>> getAllTasks(){
        List<TaskResponse> taskResponses=taskService.getAllTask();
        return ResponseBuilder.success(HttpStatus.OK, "All Task Found Successful", taskResponses);
    }

    @PostMapping("/find_ById")
    public ResponseEntity<ListResponseStructure<TaskResponse>> findById(@RequestBody CommonParam param){
        List<TaskResponse> taskResponses=taskService.findById(param);
                return ResponseBuilder.success(HttpStatus.OK, "Task Found" , taskResponses);
    }
}
