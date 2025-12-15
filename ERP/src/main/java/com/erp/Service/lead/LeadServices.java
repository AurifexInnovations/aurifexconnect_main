package com.erp.Service.lead;


import com.erp.Dto.Request.LeadRequest;
import com.erp.Dto.Request.LeadResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.Leads;

public interface LeadServices {
    Leads addOrUpdateLead(LeadRequest request);

    LeadResponse addService(LeadRequest request);

    ResultDto<LeadResponse> getAllLeads();

    LeadResponse getById(long id);

    LeadResponse deleteById(long id);

    LeadResponse updateStatus(LeadRequest leadRequest);

    ResultDto<LeadResponse> getAllBranchWise();
}
