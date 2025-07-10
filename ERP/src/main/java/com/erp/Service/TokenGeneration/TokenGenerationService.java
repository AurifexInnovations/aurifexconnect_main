package com.erp.Service.TokenGeneration;

import com.erp.Dto.Request.AuthRecord;
import com.erp.Security.JWT.ClaimName;
import com.erp.Security.JWT.TokenType;
import com.erp.Service.Helper.TokenGenerationServiceHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import java.time.Instant;
import java.util.Map;

@Service
@AllArgsConstructor
public class TokenGenerationService {

    private final TokenGenerationServiceHelper generationServiceHelper;

    public HttpHeaders grantAccessToken(AuthRecord authRecord){
        Map<String, Object> claim = setClaim(authRecord);

        String newAccessToken = generationServiceHelper.generateToken(
                TokenType.ACCESS,
                claim,
                Instant.ofEpochMilli(authRecord.accessExpiration())
        );

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add(org.springframework.http.HttpHeaders.SET_COOKIE, newAccessToken);
        return headers;
    }

    public HttpHeaders grantAccessAndRefreshToken(AuthRecord authRecord) {

        Map<String, Object> claim = setClaim(authRecord);
        String accessCookie = generationServiceHelper.generateToken(
                TokenType.ACCESS, claim, Instant.ofEpochMilli(authRecord.accessExpiration()));
        String refreshCookie = generationServiceHelper.generateToken(
                TokenType.REFRESH, claim, Instant.ofEpochMilli(authRecord.refreshExpiration()));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessCookie);
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie);
        return headers;
    }

    public Map<String, Object> setClaim(AuthRecord authRecord){
        return Map.of(
                ClaimName.USER_ID, authRecord.id(),
                ClaimName.USER_EMAIL, authRecord.email(),
                ClaimName.IS_ACTIVE, authRecord.isActive()
        );
    }
}