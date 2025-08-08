package com.erp.Service.Helper;

import com.erp.Config.AppEnv;
import com.erp.Security.JWT.JWTService;
import com.erp.Security.JWT.TokenPayload;
import com.erp.Security.JWT.TokenType;
import com.erp.Security.util.CookieManager;
import com.erp.Service.Helper.HelperNotification.TokenGenerationServiceHelperNotification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Component
@AllArgsConstructor
public class TokenGenerationServiceHelper {

    private final AppEnv env;
    private final JWTService jwtService;
    private final CookieManager cookieManager;
    private final TokenGenerationServiceHelperNotification tokenNotification;

    public String generateToken(TokenType tokenType, Map<String, Object> claim, Instant shouldExpireAt){
        TokenPayload tokenPayload = generateTokenPayload(tokenType,claim,shouldExpireAt);

        String token =jwtService.generateToken(tokenPayload);

        long maxAge = Duration.between(Instant.now(),shouldExpireAt).getSeconds();

        return cookieManager.generateCookie(tokenType.type(),token, maxAge);
    }


    private TokenPayload generateTokenPayload(TokenType type, Map<String, Object> claim, Instant shouldExpireAt) {
        Instant issueAt = calculateIssueType(type, shouldExpireAt);

        return new TokenPayload(claim, issueAt, shouldExpireAt);
    }

    private Instant calculateIssueType(TokenType type, Instant shouldExpireAt) {
        Instant issueAt;
        switch (type) {
            case ACCESS -> {
                issueAt = shouldExpireAt.minusSeconds(env.getSecurity().getTokenValidity().getAccessValidity());
            }
            case REFRESH -> {
                issueAt = shouldExpireAt.minusSeconds(env.getSecurity().getTokenValidity().getRefreshValidity());
            }
            default -> throw new IllegalArgumentException("Invalid Token Type !");
        }
        return issueAt;
    }
}