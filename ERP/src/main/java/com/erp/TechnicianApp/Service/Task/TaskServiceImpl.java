package com.erp.TechnicianApp.Service.Task;

import com.erp.TechnicianApp.Dto.Request.CommonParam;
import com.erp.TechnicianApp.Dto.Request.TaskRequest;
import com.erp.TechnicianApp.Dto.Request.TaskStatusUpdateRequest;
import com.erp.TechnicianApp.Dto.Response.TaskResponse;
import com.erp.TechnicianApp.Enum.TaskStatus;
import com.erp.TechnicianApp.Exception.InvalidStatusException;
import com.erp.TechnicianApp.Exception.InvalidStatusTransitionException;
import com.erp.TechnicianApp.Exception.TaskNotFoundById;
import com.erp.TechnicianApp.Exception.TechnicianNotFoundById;
import com.erp.TechnicianApp.Mapper.Task.TaskMapper;
import com.erp.TechnicianApp.Model.Task.Task;
import com.erp.TechnicianApp.Model.Technician.Technician;
import com.erp.TechnicianApp.Repository.Task.TaskRepository;
import com.erp.TechnicianApp.Repository.Technician.TechnicianRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TechnicianRepository technicianRepository;

    @Override
    public TaskResponse addTask(TaskRequest taskRequest) {
        Task task = taskMapper.mapToEntity(taskRequest);
        task.setStatus(TaskStatus.PENDING);
        Task savedTask = taskRepository.save(task);
        return taskMapper.mapToResponse(savedTask);
    }

    @Override
    public TaskResponse assignTaskToTechnician(Long taskId, Long technicianId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            Optional<Technician> optionalTechnician = technicianRepository.findById(technicianId);
            if (optionalTechnician.isPresent()) {
                Technician technician = optionalTechnician.get();
                task.setTechnician(technician);
                Task updatedTask = taskRepository.save(task);
                return taskMapper.mapToResponse(updatedTask);
            } else {
                throw new TechnicianNotFoundById("Technician not found with ID: " + technicianId);
            }
        } else {
            throw new TaskNotFoundById("Task not found with ID: " + taskId);
        }
    }

    @Override
    public List<TaskResponse> getTasksByTechnicianId(Long technicianId) {
        Optional<Technician> optionalTechnician = technicianRepository.findById(technicianId);

        if (optionalTechnician.isPresent()) {
            Technician technician = optionalTechnician.get();
            List<Task> tasks = taskRepository.findByTechnician_TechnicianId(technicianId);

            List<TaskResponse> taskResponses = new ArrayList<>();
            for (Task task : tasks) {
                TaskResponse taskResponse = taskMapper.mapToResponse(task);
                taskResponses.add(taskResponse);
            }
            return taskResponses;
        } else {
            throw new TechnicianNotFoundById("Technician Not found By ID" + technicianId);
        }
    }

    @Override
    public TaskResponse updateTaskStatus(Long taskId, TaskStatusUpdateRequest request) {
        Optional<Task> optionalTask=taskRepository.findById(taskId);
        if(!optionalTask.isPresent()){
            throw new TaskNotFoundById("TAsk Not found By ID :"+taskId);
        }

        Task task=optionalTask.get();

        if (task.getTechnician() == null) {
            throw new TechnicianNotFoundById("Cannot update status because this task is not assigned to any technician.");
        }

        TaskStatus newStatus;
        try{
            if(request.getStatus()==null){
                throw new InvalidStatusException("Status Is required");
            }
            newStatus=TaskStatus.valueOf(request.getStatus().trim().toUpperCase());
        }catch (IllegalArgumentException ex){
            throw new InvalidStatusException("Invalid status Value : "+request.getStatus());
        }

        TaskStatus currentStatus=task.getStatus();

        if (currentStatus == newStatus) {
            return taskMapper.mapToResponse(task);
        }
        // Validate allowed transitions
        if (!isAllowedTransition(currentStatus, newStatus)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        // Apply change
        task.setStatus(newStatus);

        // Append or set comment/feedback if provided
        String comment = request.getComment();
        if (comment != null && !comment.trim().isEmpty()) {
            String existing = task.getFeedback();
            if (existing == null || existing.trim().isEmpty()) {
                task.setFeedback(comment.trim());
            } else {
                // simple append with separator
                task.setFeedback(existing + " | " + comment.trim());
            }
        }

        // update lastUpdatedAt manually if you want (auditing may handle it)
        task.setLastUpdatedAt(LocalDateTime.now());

        Task updated = taskRepository.save(task);

        return taskMapper.mapToResponse(updated);
    }

    // simple allowed-transition rules
    private boolean isAllowedTransition(TaskStatus from, TaskStatus to) {
        if (from == null) {
            // allow setting initial status to PENDING etc.
            return true;
        }
        switch (from) {
            case PENDING:
                return to == TaskStatus.IN_PROGRESS || to == TaskStatus.CANCELLED;
            case IN_PROGRESS:
                return to == TaskStatus.COMPLETED || to == TaskStatus.CANCELLED;
            case COMPLETED:
                return false; // completed is final
            case CANCELLED:
                return false; // cancelled is final
            default:
                return false;
        }
    }
    @Override
    public TaskResponse updateTaskDetails(Long taskId, TaskRequest taskRequest) {
        Optional<Task> optionalTask= taskRepository.findById(taskId);
        if(optionalTask.isPresent()){
            Task task=optionalTask.get();

            taskMapper.updateTaskFromRequest(taskRequest, task);

            Task updatedTask=taskRepository.save(task);
            return  taskMapper.mapToResponse(updatedTask);
        }else {
            throw new TaskNotFoundById("Can not update: Task id not found "+taskId);
        }

    }

    @Override
    public List<TaskResponse> getAllTask() {
        List<Task> tasks=taskRepository.findAll();
        List<TaskResponse> taskResponses=new ArrayList<>();

        for(Task task :tasks){
            TaskResponse taskResponse=taskMapper.mapToResponse(task);
            taskResponses.add(taskResponse);
        }
        return taskResponses;

    }

    @Override
    public List<TaskResponse> findById(CommonParam param) {
        List<Task> tasks = taskRepository.findByTaskIdOrClientName(param.getId(),param.getName());
        if (tasks.isEmpty()){
            throw new TaskNotFoundById("Task Not found in DB");
        }else {
            return taskMapper.mapToResponseList(tasks);
        }
    }
}
