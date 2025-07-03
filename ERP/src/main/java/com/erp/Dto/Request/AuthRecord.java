package com.erp.Dto.Request;


public record AuthRecord(
        long id,
        String email,
        boolean isActive,
        long accessExpiration,
        long refreshExpiration
) {
}