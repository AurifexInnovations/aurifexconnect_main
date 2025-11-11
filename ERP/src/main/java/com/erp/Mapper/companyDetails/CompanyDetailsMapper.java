package com.erp.Mapper.companyDetails;

import com.erp.Dto.Request.CompanyDetailsRequestDto;
import com.erp.Dto.Response.CompanyDetailsResponse;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
import com.erp.Model.CompanyDetails;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface CompanyDetailsMapper {
    CompanyDetails toEntity(CompanyDetailsRequestDto dto);

    CompanyDetailsResponseDto toResponseDto(CompanyDetails entity);

    List<CompanyDetailsResponseDto> toResponseDtoList(List<CompanyDetails> list);
}
