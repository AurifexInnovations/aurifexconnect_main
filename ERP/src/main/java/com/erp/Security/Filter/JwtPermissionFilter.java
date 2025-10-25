package com.erp.Security.Filter;

import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.RoleActionPermission.RoleActionPermissionRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Repository.UserPermission.UserPermissionRepository;
import com.erp.Security.JWT.JWTService;
import com.erp.Security.JWT.ClaimName;
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
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtPermissionFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserPermissionRepository userPermissionRepository;
    private final RoleActionPermissionRepository roleActionPermissionRepository;
    private final UserRepository userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        if (path.equals("/api/v1/login")
                || path.startsWith("/api/v1/auth")
                || path.startsWith("/api/v1/users")
                || path.startsWith("/api/v1/admins")) {
            log.info("Skipping JwtPermissionFilter for endpoint: {}", path);
            return true;
        }

        return false; // otherwise filter runs normally
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("JwtPermissionFilter invoked for: {}", request.getRequestURI());

        try {
            String token = extractToken(request);
            if (token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("No token provided");
                return;
            }

            var claims = jwtService.parseToken(token);
            if (jwtService.isTokenExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expired, please login again");
                return;
            }

            String email = claims.get(ClaimName.USER_EMAIL, String.class);
            String schemaName = claims.get(ClaimName.SCHEMA_NAME, String.class);

            if (email == null || schemaName == null || email.isBlank() || schemaName.isBlank()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }

            TenantContext.setCurrentTenant("tenant_1_palak_gmail_com");

            // Fetch user for permission check
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found: " + email));
            Long userId = user.getId();

            String module = resolveModuleFromRequest(request);
            String action = resolveActionFromRequest(request);

            Long moduleId = roleActionPermissionRepository.findModuleIdByName(module)
                    .orElseThrow(() -> new RuntimeException("Module not found: " + module));
            Long actionId = roleActionPermissionRepository.findActionIdByName(action)
                    .orElseThrow(() -> new RuntimeException("Action not found: " + action));

            boolean hasPermission = userPermissionRepository.hasUserPermission(userId, moduleId, actionId);
            if (!hasPermission) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("You do not have permission: " + module + " / " + action);
                return;
            }

            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            log.error("Runtime exception in JwtPermissionFilter: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in JwtPermissionFilter: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Unexpected error occurred");
        } finally {
            TenantContext.clear();
        }
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("at".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String resolveModuleFromRequest(HttpServletRequest request) {
        String path = request.getRequestURI().toLowerCase();
        if (path.startsWith("/api/v1/users")) return "USER";
        if (path.startsWith("/api/v1/customers")) return "CUSTOMER";
        if (path.startsWith("/api/v1/orders")) return "ORDER";
        return "DEFAULT";
    }

    private String resolveActionFromRequest(HttpServletRequest request) {
        return switch (request.getMethod()) {
            case "GET" -> "READ";
            case "POST" -> "CREATE";
            case "PUT" -> "UPDATE";
            case "DELETE" -> "DELETE";
            default -> "UNKNOWN";
        };
    }
}
