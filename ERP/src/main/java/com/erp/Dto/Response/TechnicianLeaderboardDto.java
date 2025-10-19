package com.erp.Dto.Response;

import com.erp.Projection.MaterialUsageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianLeaderboardDto {
    private Long id;
    private String name;
    private Long tasksCompleted;
    private BigDecimal averageRating;
    private Integer rank;
    private List<MaterialUsageDto> chemicalUsage;
}
