package com.erp.Dto.Response;

import com.erp.Enum.AttendanceStatus;
import com.erp.Model.User;
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
    private Long userId;
    private String username;
    private AttendanceStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
