package com.erp.TechnicianApp.Dto.Response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse {

    private long taskId;
    private String clientName;
    private String clientLocation;
    private String serviceType;
    private LocalDateTime scheduleTime;
    private String status;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}
