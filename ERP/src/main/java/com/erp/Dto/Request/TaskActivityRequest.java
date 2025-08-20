package com.erp.Dto.Request;

import com.erp.Enum.TaskPriority;
import com.erp.Enum.TaskStatus;
import com.erp.Enum.TaskType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskActivityRequest {
    private String title;
    private String description;
    private TaskType type;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate dueDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long relatedLeadId;
    private Long relatedContactId;
    private Long relatedDealId;
}