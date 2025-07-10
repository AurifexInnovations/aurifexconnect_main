package com.erp.Security.Filter;

import com.erp.Model.GenericUser;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

        // Skip filter for public endpoints
        if (path.startsWith("/api/v1/login") || path.startsWith("/api/v1/auth/") || path.equals("/error")) {
            log.debug("Skipping AuthFilter for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        // Extract and validate token
        String token = extractToken(request);
        if (token != null && !tokenBlackListService.isBlackListed(token)) {
            try {
                Claims claims = jwtService.parseToken(token);
                String email = claims.get(ClaimName.USER_EMAIL, String.class);
                log.debug("Valid token found for email: {}", email);

                GenericUser user = userRepositoryRegistry.findUserByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Authentication set for user: {}", email);
            } catch (Exception e) {
                log.error("Token validation failed: {}", e.getMessage());
            }
        } else {
            log.debug("No valid token found for path: {}", path);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("at".equals(cookie.getName())) {
                    log.debug("Found access token cookie: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        log.debug("No access token cookie found");
        return null;
    }
}