package com.erp.Service.crm;

import com.erp.Dto.Request.LeadRequestDto;
import com.erp.Dto.Response.LeadResponseDto;
import com.erp.Mapper.crm.LeadMapper;
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
        log.info("findByCriteria called with name={}, email={}, pageable={}", name, email, pageable);
        // TODO: implement criteria search
        log.debug("findByCriteria returning empty result (not implemented)");
        return null;
    }

    @Override
    public LeadResponseDto create(LeadRequestDto dto) {
        log.info("create called with dto={}", dto);
        Lead entity = mapper.toEntity(dto);
        entity = leadRepository.save(entity);
        log.info("Lead created with id={}", entity.getId());
        return mapper.toDto(entity);
    }

    @Override
    public LeadResponseDto findById(Long id) {
        log.info("findById called with id={}", id);
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Lead not found for id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found with ID: " + id);
                });

        log.info("Lead found with id={}", id);
        return mapper.toDto(lead);
    }
}