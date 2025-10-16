package com.erp.Dto.Request;


import java.util.List;

public record AuthRecord(
        long id,
        String email,
        boolean isActive,
        String schemaName,
        long accessExpiration,
        long refreshExpiration,
        List<String> roles
) {}

