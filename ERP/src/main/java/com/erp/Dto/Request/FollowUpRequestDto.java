package com.erp.Dto.Request;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowUpRequestDto {

    private Long id;  // Optional for update
    private Long leadId;
    private Long customerId;
    private String followUpType;
    private String notes;
    private LocalDate nextFollowUpDate;
    private LocalTime nextFollowUpTime;
    private String status;
    private LocalDate completionDate;
}
