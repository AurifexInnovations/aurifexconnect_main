package com.erp.Tenant.Filter;

import com.erp.Multitenancy.TenantContext;
import com.erp.Security.JWT.ClaimName;
import com.erp.Security.JWT.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String schemaName = null;
        String token = extractToken(request);

        if (token != null) {
            try {
                schemaName = jwtService.parseToken(token).get(ClaimName.SCHEMA_NAME, String.class);
                log.debug("✅ Schema from JWT token: {}", schemaName);
            } catch (Exception e) {
                log.warn("❌ Failed to parse JWT token: {}", e.getMessage());
            }
        }

        // Optional fallback (Postman use or dev): X-Tenant-ID header
        if ((schemaName == null || schemaName.isEmpty()) && request.getHeader("X-Tenant-ID") != null) {
            schemaName = request.getHeader("X-Tenant-ID");
            log.debug("📦 Schema from X-Tenant-ID header: {}", schemaName);
        }

        // Final fallback: default schema
        if (schemaName == null || schemaName.trim().isEmpty()) {
            schemaName = "public";
            log.debug("⚠️ No schema found, using default: public");
        }
        // Set current tenant for this request
        TenantContext.setCurrentTenant(schemaName);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear(); // prevent memory leak
        }
    }

    private String extractToken(HttpServletRequest request) {
        // First: check cookie (browser clients)
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("at".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Second: check Authorization header (Postman, APIs)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
