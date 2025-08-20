package com.erp.Service.TaskActivity;

import com.erp.Dto.Request.TaskActivityRequest;
import com.erp.Dto.Response.TaskActivityResponse;
import com.erp.Enum.TaskStatus;
import com.erp.Enum.TaskType;
import com.erp.Exception.TaskActivity.TaskActivityNotFoundException;
import com.erp.Mapper.TaskActivity.TaskActivityMapper;
import com.erp.Model.TaskActivity;
import com.erp.Repository.TaskActivity.TaskActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskActivityServiceImpl implements TaskActivityService {

    private final TaskActivityRepository taskActivityRepository;
    private final TaskActivityMapper taskActivityMapper;

    @Override
    public TaskActivityResponse create(TaskActivityRequest request) {
        TaskActivity taskActivity = taskActivityMapper.mapToEntity(request);
        taskActivityRepository.save(taskActivity);
        return taskActivityMapper.mapToResponse(taskActivity);
    }

    @Override
    public TaskActivityResponse update(Long id, TaskActivityRequest request) {
        TaskActivity taskActivity = taskActivityRepository.findById(id)
                .orElseThrow(() -> new TaskActivityNotFoundException("Task Activity not found with ID: " + id));
        taskActivityMapper.mapToEntityUpdate(request, taskActivity);
        taskActivityRepository.save(taskActivity);
        return taskActivityMapper.mapToResponse(taskActivity);
    }

    @Override
    public void delete(Long id) {
        TaskActivity taskActivity = taskActivityRepository.findById(id)
                .orElseThrow(() -> new TaskActivityNotFoundException("Task Activity not found with ID: " + id));
        taskActivityRepository.delete(taskActivity);
    }

    @Override
    public TaskActivityResponse getById(Long id) {
        TaskActivity taskActivity = taskActivityRepository.findById(id)
                .orElseThrow(() -> new TaskActivityNotFoundException("Task Activity not found with ID: " + id));
        return taskActivityMapper.mapToResponse(taskActivity);
    }

    @Override
    public List<TaskActivityResponse> getAll() {
        List<TaskActivity> taskActivities = taskActivityRepository.findAll();
        if (taskActivities.isEmpty()) {
            throw new TaskActivityNotFoundException("No task activities found.");
        }
        return mapListToResponse(taskActivities);
    }

    @Override
    public List<TaskActivityResponse> getByStatus(String status) {
        TaskStatus taskStatus;

        if ("PENDING".equalsIgnoreCase(status)) {
            taskStatus = TaskStatus.PENDING;
        } else if ("COMPLETED".equalsIgnoreCase(status)) {
            taskStatus = TaskStatus.COMPLETED;
        } else if ("IN_PROGRESS".equalsIgnoreCase(status)) {
            taskStatus = TaskStatus.IN_PROGRESS;
        } else {
            throw new TaskActivityNotFoundException("Invalid TaskStatus value: " + status);
        }

        List<TaskActivity> taskActivities = taskActivityRepository.findByStatus(taskStatus);
        if (taskActivities.isEmpty()) {
            throw new TaskActivityNotFoundException("No task activities found with status: " + status);
        }
        return mapListToResponse(taskActivities);
    }

    @Override
    public List<TaskActivityResponse> getByType(String type) {
        TaskType taskType;

        if ("TO_DO".equalsIgnoreCase(type) || "TODO".equalsIgnoreCase(type)) {
            // handled both "TO_DO" and "TODO" for user-friendly input
            taskType = TaskType.TO_DO;
        } else if ("CALL".equalsIgnoreCase(type)) {
            taskType = TaskType.CALL;
        } else if ("MEETING".equalsIgnoreCase(type)) {
            taskType = TaskType.MEETING;
        } else if ("REMINDER".equalsIgnoreCase(type)) {
            taskType = TaskType.REMINDER;
        } else {
            throw new TaskActivityNotFoundException("Invalid TaskType value: " + type);
        }

        List<TaskActivity> taskActivities = taskActivityRepository.findByType(taskType);
        if (taskActivities.isEmpty()) {
            throw new TaskActivityNotFoundException("No task activities found with type: " + type);
        }
        return mapListToResponse(taskActivities);
    }

    @Override
    public List<TaskActivityResponse> getByDueDate(LocalDate date) {
        List<TaskActivity> taskActivities = taskActivityRepository.findByDueDate(date);
        if (taskActivities.isEmpty()) {
            throw new TaskActivityNotFoundException("No task activities found for due date: " + date);
        }
        return mapListToResponse(taskActivities);
    }

    @Override
    public List<TaskActivityResponse> getByRelatedLead(Long leadId) {
        List<TaskActivity> taskActivities = taskActivityRepository.findByRelatedLeadId(leadId);
        if (taskActivities.isEmpty()) {
            throw new TaskActivityNotFoundException("No task activities found for lead ID: " + leadId);
        }
        return mapListToResponse(taskActivities);
    }

    @Override
    public List<TaskActivityResponse> getByRelatedContact(Long contactId) {
        List<TaskActivity> taskActivities = taskActivityRepository.findByRelatedContactId(contactId);
        if (taskActivities.isEmpty()) {
            throw new TaskActivityNotFoundException("No task activities found for contact ID: " + contactId);
        }
        return mapListToResponse(taskActivities);
    }

    @Override
    public List<TaskActivityResponse> getByRelatedDeal(Long dealId) {
        List<TaskActivity> taskActivities = taskActivityRepository.findByRelatedDealId(dealId);
        if (taskActivities.isEmpty()) {
            throw new TaskActivityNotFoundException("No task activities found for deal ID: " + dealId);
        }
        return mapListToResponse(taskActivities);
    }

    private List<TaskActivityResponse> mapListToResponse(List<TaskActivity> taskActivities) {
        return taskActivities.stream()
                .map(taskActivityMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}