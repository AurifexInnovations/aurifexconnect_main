package com.erp.Dto.Response;

import com.erp.Dto.Request.ChemicalUsageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianDTO {
    private Long id;
    private String name;
    private Long tasksCompleted;
    private Long lateMarks;
    private Double averageRating;
    private List<ChemicalUsageDTO> chemicalUsage;
}
