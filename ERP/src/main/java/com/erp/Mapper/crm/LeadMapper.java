package com.erp.Mapper.crm;

import com.erp.Dto.Request.CustomerRequestDto;
import com.erp.Dto.Request.LeadRequestDto;
import com.erp.Dto.Response.CustomerResponseDto;
import com.erp.Dto.Response.LeadResponseDto;
import com.erp.Model.Customer;
import com.erp.Model.Lead;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeadMapper {
    Lead toEntity(LeadRequestDto dto);

    LeadResponseDto toDto(Lead entity);

}
