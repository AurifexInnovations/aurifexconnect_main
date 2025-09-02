package com.erp.TechnicianApp.TechnicianDto.Request;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LocationImageDTO {
    private Long technicianId;
    private Long taskId;
    private Double latitude;
    private Double longitude;
    private String note;

    private MultipartFile image; // uploaded proof
}
