package com.erp.Dto.Request;

public record ServiceDocumentRequestDto(
        Long id,
        String documentName,
        String documentUrl
) {}
