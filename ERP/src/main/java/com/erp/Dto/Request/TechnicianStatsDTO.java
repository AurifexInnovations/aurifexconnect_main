package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianStatsDTO {
    private Long technicianId;
    private Long completedTasks;
    private Double averageRating;
    private Long tasksCompleted;
}
