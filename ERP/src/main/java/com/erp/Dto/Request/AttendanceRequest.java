package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceRequest {
    private long id;
    private Long userId;
    private String month;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String status;
}