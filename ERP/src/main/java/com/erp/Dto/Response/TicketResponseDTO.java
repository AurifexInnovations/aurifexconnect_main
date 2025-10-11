package com.erp.Dto.Response;

import com.erp.Enum.Priority;
import com.erp.Enum.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketResponseDTO {

    private Long id;
    private Long technicianId;
    private Long customerId;
    private String customerLocation;
    private Long taskId;
    private String issueDescription;
    private Priority priority;
    private TicketStatus ticketStatus;
}
