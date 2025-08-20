package com.erp.Service.TokenGeneration.TokenGenerationNotification;

import com.erp.Dto.Request.AuthRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenGenerationNotification {

    public void notifyAccessTokenGenerated(AuthRecord authRecord) {
        log.info("✅ Access token generated for user: ID={}, Email={}, Active={}",
                authRecord.id(), authRecord.email(), authRecord.isActive());
    }

    public void notifyRefreshTokenGenerated(AuthRecord authRecord) {
        log.info("🔄 Refresh token generated for user: ID={}, Email={}, Active={}",
                authRecord.id(), authRecord.email(), authRecord.isActive());
    }

    public void notifyBothTokensGenerated(AuthRecord authRecord) {
        log.info("🔐 Both Access and Refresh tokens generated for user: ID={}, Email={}",
                authRecord.id(), authRecord.email());
    }
}
