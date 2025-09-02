package com.erp.TechnicianApp.TechnicianDto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TechnicianShiftResponse {

    private Long shiftId;
    private Long userId;
    private String shiftName;
    private String shiftType;
    private LocalDateTime shiftStart;
    private LocalDateTime shiftEnd;
    private boolean active;
    private String startTime;
    private String endTime;
}