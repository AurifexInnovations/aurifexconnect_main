package com.erp.Service.DocumentDetails;

import com.erp.Dto.Request.DocumentDetailsRequestDto;
import com.erp.Dto.Response.DocumentDetailsResponseDto;
import com.erp.Mapper.DocumentDetails.DocumentDetailsMapper;
import com.erp.Model.DocumentDetails;
import com.erp.Repository.DocumentDetails.DocumentDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class DocumentDetailsServiceImpl implements DocumentDetailsService {
    private final DocumentDetailsMapper documentDetailsMapper;
    private final DocumentDetailsRepository documentDetailsRepository;

    @Override
    public Optional<DocumentDetailsResponseDto> findById(final Long id) {
        return documentDetailsRepository.findById(id)
                .map(documentDetailsMapper::toResponseDto);

    }

    @Override
    public List<DocumentDetailsResponseDto> saveAndUpdate(List<DocumentDetailsRequestDto> documentDetailsDtos) {
        log.info("Into [DocumentDetailsService] [saveAndUpdate] - Starting to add/update document details");

        try {
            // convert list of request DTOs to entities
            final List<DocumentDetails> newEntities = documentDetailsMapper.toEntity(documentDetailsDtos);

            // saveAll returns a List<DocumentDetails>
            final List<DocumentDetails> savedEntities = documentDetailsRepository.saveAll(newEntities);

            // map saved entities to response DTOs
            return savedEntities.stream()
                    .map(documentDetailsMapper::toResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Exception occurred in [DocumentDetailsService] [saveAndUpdate]: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save or update document details", e);
        }
    }

}