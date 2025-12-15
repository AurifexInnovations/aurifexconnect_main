package com.erp.Dto.Response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowUpResponseDto {

    private Long id;
    private Long leadId;
    private Long quotationId;

    private String followUpType;
    private String status;

    private String notes;
    private LocalDate nextFollowupDate;
    private LocalTime nextFollowupTime;

    private String lostReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private long branchId;
}
