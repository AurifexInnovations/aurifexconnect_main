package com.erp.TechnicianApp.TechnicianDto.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TechnicianLocationImageRequest {
    private Long userId;
    private Long attendanceId;   // 🔹 link to Attendance
    private Long taskId;
    private Double latitude;
    private Double longitude;
    private String note;
    private MultipartFile image; // uploaded proof
}
