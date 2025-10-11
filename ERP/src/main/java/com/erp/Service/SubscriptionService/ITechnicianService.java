package com.erp.Service.SubscriptionService;

import com.erp.Dto.Request.TechnicianRequest;
import com.erp.Dto.Response.TechnicianResponse;
import com.erp.Dto.SubscriptionsDto.TechnicianDto;

public interface ITechnicianService {
    TechnicianDto fetchTechnicianById(Long technicianId);

    TechnicianResponse createTechnician(TechnicianRequest request);
}
