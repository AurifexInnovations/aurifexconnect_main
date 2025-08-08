package com.erp.Security.Filter;

import com.erp.Exception.User.UserNotFoundException;
import com.erp.Model.GenericUser;
import com.erp.Multitenancy.TenantContext;
import com.erp.Security.JWT.ClaimName;
import com.erp.Security.JWT.JWTService;
import com.erp.Security.util.UserRepositoryRegistry;
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
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final TokenBlackListService tokenBlackListService;
    private final UserRepositoryRegistry userRepositoryRegistry;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        log.debug("Processing request in AuthFilter: {}", path);

        // Allow unauthenticated public endpoints
        if (path.startsWith("/api/v1/login") || path.startsWith("/api/v1/auth/") || path.equals("/error")) {
            log.debug("Skipping AuthFilter for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);

        if (token != null && !tokenBlackListService.isBlackListed(token)) {
            try {
                Claims claims = jwtService.parseToken(token);
                String email = claims.get(ClaimName.USER_EMAIL, String.class);
                String schemaName = claims.get(ClaimName.SCHEMA_NAME, String.class);
                List<String> roles = claims.get(ClaimName.ROLE, List.class); // ✅ CORRECT

                if (schemaName == null || schemaName.isBlank()) {
                    log.error("Missing schema name in token for email: {}", email);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token: missing schema");
                    return;
                }

                // Set schema BEFORE accessing DB
                TenantContext.setCurrentTenant(schemaName);
                log.debug("Tenant set to: {}", schemaName);

                GenericUser user = userRepositoryRegistry.findUserByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException("User not found: " + email));

                // Build authorities list from JWT role
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList(); // ✅ correct mapping

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Authenticated user: {} with role: {} on schema: {}", email, roles, schemaName);

            } catch (Exception e) {
                log.error("AuthFilter failed: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
                return;
            }
        } else {
            log.debug("No valid token found or token is blacklisted for path: {}", path);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();  // Always clear context after request
        }
    }

    private String extractToken(HttpServletRequest request) {
        // 1. Check Authorization header first
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.debug("Found access token in Authorization header");
            return token;
        }

        // 2. Fallback to checking the 'at' cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("at".equals(cookie.getName())) {
                    log.debug("Found access token in cookie");
                    return cookie.getValue();
                }
            }
        }

        log.debug("No access token found in header or cookie");
        return null;
    }

}
