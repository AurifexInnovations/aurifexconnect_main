package com.erp.Dto.Response;

import com.erp.Dto.Request.LeaderboardDTO;
import com.erp.Dto.Request.TechnicianStatsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianPerformanceResponse {
    private List<TechnicianStatsDTO> technicians;
    private List<LeaderboardDTO> leaderboard;
}