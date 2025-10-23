package com.erp.Dto.Request;

public record LeadRequestDto(Long id,
                             String name,
                             String contact,
                             String status,
                             String source,
                             String teg,
                             String engagement) { }
