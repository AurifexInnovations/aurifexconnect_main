package com.erp.Service.DocumentDetails;

import com.erp.Dto.Request.DocumentDetailsRequestDto;
import com.erp.Dto.Response.DocumentDetailsResponseDto;

import java.util.List;
import java.util.Optional;

public interface DocumentDetailsService {

    Optional<DocumentDetailsResponseDto> findById(Long id);

    List<DocumentDetailsResponseDto> saveAndUpdate(List<DocumentDetailsRequestDto> DocumentDetails);
}
