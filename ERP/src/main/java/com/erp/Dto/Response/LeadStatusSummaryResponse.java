package com.erp.Dto.Response;

import lombok.Data;

@Data
public class LeadStatusSummaryResponse {
    private Long newCount;
    private Long contactedCount;
    private Long qualifiedCount;
    private Long lostCount;
}
