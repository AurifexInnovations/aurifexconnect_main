package com.erp.TechnicianApp.TechnicianDto.Response;

import com.erp.TechnicianApp.TechnicianModel.TechnicianTask.TechnicianTaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TechnicianTaskResponse {

    private Long taskId;

    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    private String title;
    private String description;

    private TechnicianTaskStatus status;

    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;

    private String clientName;
    private String clientLocation;
    private String serviceType;

    private LocalDateTime scheduledAt;
    private LocalDateTime scheduledFor;
    private LocalDateTime startedAt;

    private Double jobLatitude;
    private Double jobLongitude;
    private Integer jobRadiusMeters;

    private String feedback;
}
