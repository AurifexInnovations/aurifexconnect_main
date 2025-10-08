package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianStatsDTO {
    private Long technicianId;
    private Long completedTasks;
    private Double averageRating;
    private Long tasksCompleted;
    private List<ChemicalUsageDTO> chemicalUsage;


}
