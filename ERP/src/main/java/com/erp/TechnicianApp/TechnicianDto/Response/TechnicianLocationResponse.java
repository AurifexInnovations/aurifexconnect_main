package com.erp.TechnicianApp.TechnicianDto.Response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TechnicianLocationResponse {
    private Long locationId;
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
    private String imageUrl;
    private LocalDateTime recordedAt;
}
