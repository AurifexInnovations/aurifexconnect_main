package com.erp.TechnicianApp.Dto.Request;

import com.erp.Enum.AttendanceStatus;

import com.erp.TechnicianApp.Enum.TechnicianDayStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TechnicianAttendanceRequest {

    private Long technicianId; // ID of technician
    private Long taskId; // optional - null if only daily attendance

    private Double checkInLatitude;
    private Double checkInLongitude;
    private Double checkOutLatitude;
    private Double checkOutLongitude;

    private String notes;

    private TechnicianDayStatus dayStatus; // PRESENT, ABSENT, LEAVE, HOLIDAY
    private AttendanceStatus status; // CHECK_IN, CHECK_OUT

    // Multiple images for check-in
    private MultipartFile[] checkInImages;

    // Multiple images for check-out
    private MultipartFile[] checkOutImages;
}
