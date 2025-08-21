package com.erp.TechnicianApp.Service.Technician;


import com.erp.TechnicianApp.Dto.Request.CommonParam;
import com.erp.TechnicianApp.Dto.Request.TechnicianRequest;
import com.erp.TechnicianApp.Dto.Response.TechnicianResponse;

import java.util.List;

public interface TechnicianService {
    TechnicianResponse addTechnician(TechnicianRequest technicianRequest);

    List<TechnicianResponse> getAllTechnician();

    List<TechnicianResponse> findTechnicianById(CommonParam param);

    TechnicianResponse updateTechnicianById(long id, TechnicianRequest technicianRequest);

    TechnicianResponse deleteTechnicianById(long id);
}
