package com.erp.Mapper.companyDetails;

import com.erp.Dto.Request.CompanyDetailsRequestDto;
import com.erp.Dto.Response.CompanyDetailsResponse;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
import com.erp.Model.CompanyDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface CompanyDetailsMapper {
    CompanyDetails toEntity(CompanyDetailsRequestDto dto);

    CompanyDetailsResponseDto toResponseDto(CompanyDetails entity);

    List<CompanyDetailsResponseDto> toResponseDtoList(List<CompanyDetails> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "documentDetails", ignore = true)
    void updateEntityFromEntity(CompanyDetails source, @MappingTarget CompanyDetails target);
}
