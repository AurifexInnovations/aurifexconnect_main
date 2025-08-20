package com.erp.Mapper.Lead;


import com.erp.Dto.Request.LeadRequest;
import com.erp.Dto.Response.LeadResponse;
import com.erp.Model.Lead;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeadMapper {

    LeadResponse mapToResponse(Lead lead);

    List<LeadResponse> mapToResponseList(List<Lead> leads);

    Lead mapToEntity(LeadRequest request);

    void updateEntityFromRequest(LeadRequest request, @MappingTarget Lead lead);
}
