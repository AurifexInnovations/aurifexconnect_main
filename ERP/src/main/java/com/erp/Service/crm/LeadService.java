package com.erp.Service.crm;

import com.erp.Dto.Request.LeadRequestDto;
import com.erp.Dto.Response.LeadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeadService {
    Page<LeadResponseDto> findByCriteria(String name, String email, Pageable pageable);

    LeadResponseDto create(LeadRequestDto dto);

    LeadResponseDto findById(Long id);
}
