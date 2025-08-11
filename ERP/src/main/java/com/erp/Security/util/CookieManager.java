package com.erp.Security.util;

import com.erp.Config.AppEnv;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@AllArgsConstructor
public class CookieManager {

    private final AppEnv env;

    public String generateCookie(String name, String value, long maxAge) {
        return ResponseCookie.from(name, value)
//                .domain(env.getDomain().getName())
                .path("/")
                .sameSite(env.getDomain().getSameSite())
                .httpOnly(true)
                .secure(env.getDomain().isSecure())
                .maxAge(maxAge)
                .build()
                .toString();
    }
}