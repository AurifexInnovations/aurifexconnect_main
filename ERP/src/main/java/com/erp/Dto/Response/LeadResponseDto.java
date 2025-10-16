package com.erp.Dto.Response;

public record LeadResponseDto(Long id,
                              String companyName,
                              String contact,
                              String status,
                              String source,
                              String teg,
                              String engagement) {
}
