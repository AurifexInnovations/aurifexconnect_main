package com.erp.Service.lead;


import com.erp.Dto.Request.LeadRequest;
import com.erp.Model.Leads;

public interface LeadServices {
    Leads addOrUpdateLead(LeadRequest request);
}
