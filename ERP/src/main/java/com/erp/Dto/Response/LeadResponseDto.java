package com.erp.Dto.Response;

public record LeadResponseDto(Long id,
                              String name,
                              String contact,
                              String status,
                              String source,
                              String teg,
                              String engagement) {
}
