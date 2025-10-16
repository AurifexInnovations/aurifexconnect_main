package com.erp.Dto.Response;

import com.erp.Model.Lead;

public record CustomerResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        String company,
        String engagement,
        Lead lead
) {
}