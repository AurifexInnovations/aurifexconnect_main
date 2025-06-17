package com.erp.Dto.Response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceResponse {
    private long id;
    private LocalDate date;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String workingHours;
    private String workingDays;
}
