package com.erp.Service.crm;

import com.erp.Dto.Request.CustomerRequestDto;
import com.erp.Dto.Request.LeadRequestDto;
import com.erp.Dto.Response.CustomerResponseDto;
import com.erp.Dto.Response.LeadResponseDto;
import com.erp.Mapper.crm.LeadMapper;
import com.erp.Model.Customer;
import com.erp.Model.Lead;
import com.erp.Repository.crm.LeadRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@Slf4j
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final LeadMapper mapper;

    @Override
    public Page<LeadResponseDto> findByCriteria(String name, String email, Pageable pageable) {
        return null;
    }

    @Override
    public LeadResponseDto create(LeadRequestDto dto) {
        Lead entity = mapper.toEntity(dto);
        entity = leadRepository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public LeadResponseDto findById(Long id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found with ID: " + id));

        return mapper.toDto(lead); // Convert to LeadResponseDto
    }
}
