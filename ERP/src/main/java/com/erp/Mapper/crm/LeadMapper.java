package com.erp.Mapper.crm;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.LeadResponse;
import com.erp.Dto.Response.CustomerResponseDto;
import com.erp.Dto.Response.LeadResponseDto;
import com.erp.Model.Customer;
import com.erp.Model.Lead;
import com.erp.Model.LeadProductMapper;
import com.erp.Model.Leads;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeadMapper {
    Lead toEntity(LeadRequestDto dto);

    LeadResponseDto toDto(Lead entity);

    Leads toLeadsDto(LeadRequest leadRequest);

    LeadResponse toResponseDto(Leads leads);

    List<LeadProductRequestDto> toListResponseProducts(List<LeadProductMapper> leadProductMapper);
}
