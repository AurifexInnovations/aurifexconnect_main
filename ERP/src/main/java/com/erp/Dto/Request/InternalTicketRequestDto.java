package com.erp.Dto.Request;

import com.erp.Enum.Priority;
import com.erp.Enum.TicketStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternalTicketRequestDto {

    private Long id;
    private String title;
    private Priority priority;
    private TicketStatus status;
    private Long assignedTo;

}
