package com.erp.Dto.Response;

import com.erp.Dto.Request.ChemicalUsageDTO;
import com.erp.Dto.Request.LeaderboardDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianPerformanceDTO {

    private String name;
    private Long technicianId;
    private Long completedTasks;
    private Double averageRating;
    private Long tasksCompleted;
    private List<LeaderboardDTO> leaderboardDTO;
    private List<ChemicalUsageDTO> chemicalUsage;


}
