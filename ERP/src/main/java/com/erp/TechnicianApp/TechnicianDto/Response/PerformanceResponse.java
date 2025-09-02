package com.erp.TechnicianApp.TechnicianDto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
public class PerformanceResponse {

    private Long performanceId;

    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    private Integer month;
    private Integer year;

    private BigDecimal punctualityScore;
    private BigDecimal taskCompletionScore;
    private BigDecimal overtimeScore;
    private BigDecimal customerFeedbackScore;
    private BigDecimal finalRating;
}
