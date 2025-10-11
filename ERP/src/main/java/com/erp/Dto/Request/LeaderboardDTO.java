package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardDTO {

    private Long technicianId;
    private String name;
    private Integer rank;
    private Double averageRating;

}
