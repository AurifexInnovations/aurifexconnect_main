package com.erp.Service.Helper.HelperNotification;

import com.erp.Security.JWT.TokenType;

import java.time.Instant;
import java.util.Map;

public interface TokenGenerationServiceHelperNotification {
    void notifyTokenGenerated(TokenType tokenType, Map<String, Object> claims, Instant expiry);
}
