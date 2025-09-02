package com.erp.TechnicianApp.TechnicianDto.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TechnicianLocationRequest {
    private Long userId;
    private Long attendanceId;
    private Long taskId;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Float speed;
    private Float bearing;
    private Float accuracy;
    private String provider;
    private Integer batteryLevel;
    private String note;
    private LocalDateTime recordedAt;
}
