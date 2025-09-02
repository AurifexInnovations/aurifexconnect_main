package com.erp.TechnicianApp.TechnicianDto.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TechnicianTaskRequest {

    private Long userId;

    private String title;
    private String description;

    private String clientName;
    private String clientLocation;
    private String serviceType;

    private LocalDateTime scheduledAt;
    private LocalDateTime scheduledFor;

    private Double jobLatitude;
    private Double jobLongitude;
    private Integer jobRadiusMeters;
}
