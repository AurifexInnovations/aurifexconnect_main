package com.erp.Dto.Response;

import com.erp.Enum.Priority;
import com.erp.Enum.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketViewDTO {

    private Long ticketId;
    private String technicianName;
    private String location;
    private String taskName;
    private String customerName;
    private String contact;
    private String issueDescription;
    private Priority priority;
    private TicketStatus ticketStatus;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
