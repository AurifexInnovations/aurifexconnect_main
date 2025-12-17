package com.erp.Dto.Request;


import com.erp.Enum.Priority;
import com.erp.Enum.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketRequestDTO {

    private Long id;
    private Long technicianId;
    private Long customerId;
    private String customerLocation;
    private Long taskId;
    private String issueDescription;
    private Priority priority;
    private TicketStatus ticketStatus;
    private LocalDate assignedDate;
    private LocalTime assignedTime;
}
