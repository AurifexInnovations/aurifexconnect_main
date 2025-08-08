package com.erp.Security.Filter;

import com.erp.Multitenancy.TenantContext;
import com.erp.Security.JWT.ClaimName;
import com.erp.Security.JWT.JWTService;
import com.erp.Security.JWT.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class RefreshAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final TokenBlackListService tokenBlackListService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        log.debug("RefreshAuthFilter processing request: {}", path);
        log.debug("Looking for token type: {}", TokenType.REFRESH.type());

        // Skip filter for public/auth/logout paths
        if (path.startsWith("/api/v1/login") || path.startsWith("/api/v1/auth/")
                || path.equals("/error") || path.equals("/api/v1/logout")) {
            log.debug("Skipping RefreshAuthFilter for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        String token = cookies != null ? FilterHelper.extractTokenFromCookie(cookies, TokenType.REFRESH) : null;

        if (token != null && !tokenBlackListService.isBlackListed(token)) {
            log.info("Refresh token found and is not blacklisted");

            try {
                Claims claims = jwtService.parseToken(token);
                String email = claims.get(ClaimName.USER_EMAIL, String.class);
                String schemaName = claims.get(ClaimName.SCHEMA_NAME, String.class);
                log.debug("Token claims extracted: email = {}, schema = {}", email, schemaName);

                TenantContext.setCurrentTenant(schemaName != null ? schemaName : "public");

                if (email != null && !email.isEmpty()) {
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        List<SimpleGrantedAuthority> authorities = List.of(
                                new SimpleGrantedAuthority("ROLE_REFRESH")
                        );

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                        );

                        authToken.setDetails(request);
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        log.info("Refresh authentication set for user: {}", email);
                    }
                } else {
                    log.error("Invalid email in token claims");
                }

            } catch (Exception e) {
                log.error("Failed to validate refresh token: {}", e.getMessage());
                TenantContext.clear(); // ensure thread cleanup
            }

        } else {
            log.warn("Refresh token not found or is blacklisted");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear(); // Always clean up thread-local
        }
    }
}
