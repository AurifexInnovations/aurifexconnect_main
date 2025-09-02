package com.erp.TechnicianApp.TechnicianDto.Request;

import jakarta.persistence.Column;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LocationDTO {
    private Long technicianId;
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

    @Column(name = "image_url", length = 500)
    private String imageUrl;
}