package com.erp.TechnicianApp.Dto.Response;

import com.erp.Enum.AttendanceStatus;
import com.erp.TechnicianApp.Enum.TechnicianDayStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TechnicianAttendanceResponse {

    private Long attendanceId;

    private Long technicianId;
    private String technicianName;

    private Long taskId;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private Double checkInLatitude;
    private Double checkInLongitude;
    private Double checkOutLatitude;
    private Double checkOutLongitude;

    private String notes;

    private TechnicianDayStatus dayStatus;
    private AttendanceStatus status;

}




