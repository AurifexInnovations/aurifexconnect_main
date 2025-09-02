package com.erp.TechnicianApp.TechnicianService.Task;

import com.erp.Model.User;
import com.erp.Repository.User.UserRepository;
import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianTaskRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.TaskStatusUpdateRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianTaskResponse;
import com.erp.TechnicianApp.TechnicianEnum.TechnicianTaskStatus;
import com.erp.TechnicianApp.TechnicianMapper.Task.TechnicianTaskMapper;
import com.erp.TechnicianApp.TechnicianModel.TechnicianTask;
import com.erp.TechnicianApp.TechnicianRepository.TechnicianTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianTaskServiceImpl implements TechnicianTaskService {

    private final TechnicianTaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TechnicianTaskMapper mapper;

    @Override
    public TechnicianTaskResponse createTask(TechnicianTaskRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TechnicianTask task = mapper.toEntity(request);
        task.setUser(user);
        task.setStatus(TechnicianTaskStatus.PENDING);
        task.setAssignedAt(LocalDateTime.now());

        return mapper.toResponse(taskRepository.save(task));
    }

    @Override
    public TechnicianTaskResponse assignTaskToTechnician(Long taskId, Long technicianId) {
        TechnicianTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        task.setUser(technician);
        task.setAssignedAt(LocalDateTime.now());

        return mapper.toResponse(taskRepository.save(task));
    }

    @Override
    public List<TechnicianTaskResponse> getTasksByTechnicianId(Long technicianId) {
        List<TechnicianTask> tasks = taskRepository.findByUserId(technicianId);
        return mapper.toResponseList(tasks);
    }

    @Override
    public TechnicianTaskResponse updateTaskStatus(Long taskId, TaskStatusUpdateRequest request) {
        TechnicianTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(request.getStatus());
        if (request.getStatus() == TechnicianTaskStatus.COMPLETED) {
            task.setCompletedAt(LocalDateTime.now());
        }

        return mapper.toResponse(taskRepository.save(task));
    }

    @Override
    public TechnicianTaskResponse updateTaskDetails(Long taskId, TechnicianTaskRequest request) {
        TechnicianTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getClientName() != null) task.setClientName(request.getClientName());
        if (request.getClientLocation() != null) task.setClientLocation(request.getClientLocation());
        if (request.getServiceType() != null) task.setServiceType(request.getServiceType());
        if (request.getScheduledAt() != null) task.setScheduledAt(request.getScheduledAt());
        if (request.getScheduledFor() != null) task.setScheduledFor(request.getScheduledFor());
        if (request.getJobLatitude() != null) task.setJobLatitude(request.getJobLatitude());
        if (request.getJobLongitude() != null) task.setJobLongitude(request.getJobLongitude());
        if (request.getJobRadiusMeters() != null) task.setJobRadiusMeters(request.getJobRadiusMeters());

        return mapper.toResponse(taskRepository.save(task));
    }

    @Override
    public List<TechnicianTaskResponse> getAllTasks() {
        return mapper.toResponseList(taskRepository.findAll());
    }

    @Override
    public TechnicianTaskResponse getTaskById(Long taskId) {
        TechnicianTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapper.toResponse(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        TechnicianTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}
