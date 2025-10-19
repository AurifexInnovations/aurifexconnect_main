package com.erp.Mapper.companyDetails;

import com.erp.Dto.Request.CompanyDetailsRequestDto;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
import com.erp.Model.CompanyDetails;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CompanyDetailsMapper {
    CompanyDetails toEntity(CompanyDetailsRequestDto dto);

    CompanyDetailsResponseDto toResponseDto(CompanyDetails entity);
}
