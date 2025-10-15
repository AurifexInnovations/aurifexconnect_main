package com.erp.Dto.Response;

import com.erp.Enum.Priority;
import com.erp.Enum.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO used for sending internal ticket details in responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternalTicketResponseDto {

    private Long id;

    private String title;

    private Priority priority;

    private TicketStatus status;

    private Long assignedTo;

    private Long createdBy;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdated;

}
