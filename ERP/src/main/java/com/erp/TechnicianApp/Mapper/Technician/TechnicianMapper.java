package com.erp.TechnicianApp.Mapper.Technician;


import com.erp.TechnicianApp.Dto.Request.TechnicianRequest;
import com.erp.TechnicianApp.Dto.Response.TechnicianResponse;
import com.erp.TechnicianApp.Model.Technician.Technician;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface TechnicianMapper {

    Technician mapToEntity (TechnicianRequest technicianRequest);
    TechnicianResponse mapToResponse(Technician technician);
    List<TechnicianResponse> mapToResponse(List<Technician> technician);
    void updateTechnicianFromRequest(TechnicianRequest technicianRequest, @MappingTarget Technician technician);

}
