package com.erp.TechnicianApp.TechnicianDto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianShiftRequest {
    private Long userId;         // Technician/User ID
    private LocalDate date;      // Date of the shift
    private LocalTime startTime; // Shift start time
    private LocalTime endTime;   // Shift end time
    private String shiftType;    // e.g., MORNING, EVENING, NIGHT
}
