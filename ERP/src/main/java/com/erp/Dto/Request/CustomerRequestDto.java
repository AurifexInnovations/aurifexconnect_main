package com.erp.Dto.Request;

import com.erp.Model.Lead;

public record CustomerRequestDto(
        Long id,
        String name,
        String email,
        String phone,
        String company,
        String engagement,
        LeadRequestDto lead
) {
}