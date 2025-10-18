package com.erp.Dto.Response;

import com.erp.Enum.AttendanceStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttendanceResponse {
    private long id;
    private LocalDate date;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String workingHours;
    private String workingDays;
    private long userId;
    private String userName;
    private AttendanceStatus status;

}
