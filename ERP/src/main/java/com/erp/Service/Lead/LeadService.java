package com.erp.Service.Lead;

import com.erp.Dto.Request.LeadRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.LeadMonthlyChartResponse;
import com.erp.Dto.Response.LeadResponse;
import com.erp.Dto.Response.LeadStatusSummaryResponse;


import java.util.List;

public interface LeadService {

    LeadResponse create(LeadRequest request);

    LeadResponse update(Long id, LeadRequest request);

    List<LeadResponse> getAll();

    LeadResponse getById(Long id);

    void delete(Long id);

    LeadResponse convertToContact(Long id);


    List<LeadMonthlyChartResponse> getMonthlyLeadsChart(Param param);

    LeadStatusSummaryResponse getLeadStatusSummary(Param param);
}