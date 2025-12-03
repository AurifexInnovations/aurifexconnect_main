package com.erp.Security.Filter;

import com.erp.Exception.ErrorResponse;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Model.GenericUser;
import com.erp.Multitenancy.TenantContext;
import com.erp.Security.JWT.ClaimName;
import com.erp.Security.JWT.JWTService;
import com.erp.Security.util.UserRepositoryRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@AllArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final TokenBlackListService tokenBlackListService;
    private final UserRepositoryRegistry userRepositoryRegistry;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        log.debug("Processing request in AuthFilter: {}", path);

        // Allow unauthenticated endpoints
        if (path.startsWith("/api/v1/login") || path.startsWith("/api/v1/auth/") || path.equals("/error") || path.startsWith("/api/v1/admins")) {
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
                List<String> roles = claims.get(ClaimName.ROLE, List.class);

                if (schemaName == null || schemaName.isBlank()) {
                    sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Invalid token: missing schema");
                    return;
                }

                TenantContext.setCurrentTenant(schemaName);
                GenericUser user = userRepositoryRegistry.findUserByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException("User not found: " + email));

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Authenticated user: {} with role: {} on schema: {}", email, roles, schemaName);

            } catch (UserNotFoundException e) {
                sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            } catch (Exception e) {
                sendJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Unauthorized or invalid token");
                return;
            }
        } else {
            sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing or blacklisted token");
            return;
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

    private void sendJsonError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);

        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
