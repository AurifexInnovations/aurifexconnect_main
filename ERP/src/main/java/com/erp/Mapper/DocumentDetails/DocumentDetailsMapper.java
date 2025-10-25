package com.erp.Mapper.DocumentDetails;

import com.erp.Dto.Request.DocumentDetailsRequestDto;
import com.erp.Dto.Response.DocumentDetailsResponseDto;
import com.erp.Model.DocumentDetails;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface DocumentDetailsMapper {
    DocumentDetails toEntity(DocumentDetailsRequestDto dto);

    DocumentDetailsResponseDto toResponseDto(DocumentDetails entity);

    List<DocumentDetails> toEntity(List<DocumentDetailsRequestDto> dtos);

    List<DocumentDetailsResponseDto> toResponseDto(List<DocumentDetails> entities);
}
