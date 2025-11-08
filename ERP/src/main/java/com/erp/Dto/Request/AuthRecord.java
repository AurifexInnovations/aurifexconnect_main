package com.erp.Dto.Request;


import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public record AuthRecord(
        long id,
        String email,
        boolean isActive,
        String schemaName,
        long accessExpiration,
        long refreshExpiration,
        List<String> roles,
        String token,
        String refreshToken
) {}

