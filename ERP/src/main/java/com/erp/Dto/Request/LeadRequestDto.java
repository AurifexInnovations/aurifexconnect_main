package com.erp.Dto.Request;

public record LeadRequestDto(Long id,
                             String companyName,
                             String contact,
                             String status,
                             String source,
                             String teg,
                             String engagement) { }
