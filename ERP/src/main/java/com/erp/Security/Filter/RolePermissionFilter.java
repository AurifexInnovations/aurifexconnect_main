package com.erp.Security.Filter;

import com.erp.Exception.ResourceFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.Action;
import com.erp.Model.Module;
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
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class RolePermissionFilter extends OncePerRequestFilter {

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
                || path.startsWith("/api/v1/admins") || path.startsWith("/api/v1/admin")) {
            log.info("Skipping JwtPermissionFilter for endpoint: {}", path);
            return true;
        }

        return false; // otherwise filter runs normally
    }

    private String getHttpStatusText(int status) {
        switch (status) {
            case HttpServletResponse.SC_UNAUTHORIZED:
                return "UNAUTHORIZED";
            case HttpServletResponse.SC_FORBIDDEN:
                return "FORBIDDEN";
            case HttpServletResponse.SC_NOT_FOUND:
                return "NOT_FOUND";
            case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                return "INTERNAL_SERVER_ERROR";
            default:
                return "ERROR";
        }
    }


    private void writeJsonError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format(
                "{\"status\": %d, \"error\": \"%s\", \"message\": \"%s\"}",
                status,
                getHttpStatusText(status),
                message.replace("\"", "'")
        );

        response.getWriter().write(jsonResponse);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws  IOException {

        log.info("JwtPermissionFilter invoked for: {}", request.getRequestURI());

        try {


            String token = extractToken(request);
            if (token == null) {
                writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "No token provided");
                return;
            }

            var claims = jwtService.parseToken(token);
            if (jwtService.isTokenExpired(token)) {
                writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expired, please login again");
                return;
            }

            String email = claims.get(ClaimName.USER_EMAIL, String.class);
            String schemaName = claims.get(ClaimName.SCHEMA_NAME, String.class);

            if (email == null || schemaName == null || email.isBlank() || schemaName.isBlank()) {
                writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }

            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

            Long userId = user.getId();
            TenantContext.setCurrentTenant(user.getSchemaName());

            String moduleIdHeader = request.getHeader("moduleId");
            String actionIdHeader = request.getHeader("actionId");

            long moduleId = (moduleIdHeader != null && !moduleIdHeader.isBlank())
                    ? Long.parseLong(moduleIdHeader)
                    : 0L;

            long actionId = (actionIdHeader != null && !actionIdHeader.isBlank())
                    ? Long.parseLong(actionIdHeader)
                    : 0L;


            Module module = roleActionPermissionRepository.findModuleId((moduleId));
            if (Objects.isNull(module)) {
                writeJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Module not found for this user");
                return;
            }

            Action action = roleActionPermissionRepository.findActionId((actionId));
            if (Objects.isNull(action)) {
                writeJsonError(response, HttpServletResponse.SC_NOT_FOUND, "Action not found for this user");
                return;
            }

            boolean hasPermission = userPermissionRepository
                    .hasUserPermission(userId, moduleId, (actionId));

            if (!hasPermission) {
                writeJsonError(response, HttpServletResponse.SC_FORBIDDEN,
                        "You do not have permission   module :  " + module.getName() + " / action: " + action.getName());
                return;
            }

            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            log.error("Runtime exception in JwtPermissionFilter: {}", e.getMessage(), e);
            writeJsonError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal Server Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in JwtPermissionFilter: {}", e.getMessage(), e);
            writeJsonError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred");
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

//    private String resolveModuleFromRequest(HttpServletRequest request) {
//        String path = request.getRequestURI().toLowerCase();
//        if (path.startsWith("/api/v1/users")) return "USER";
//        if (path.startsWith("/api/v1/customers")) return "CUSTOMER";
//        if (path.startsWith("/api/v1/orders")) return "ORDER";
//        if (path.startsWith("/api/v1/module")) return "TEST";
//
//        return "DEFAULT";
//    }
//
//    private String resolveActionFromRequest(HttpServletRequest request) {
//        return switch (request.getMethod()) {
//            case "GET" -> "READ";
//            case "POST" -> "CREATE";
//            case "PUT" -> "UPDATE";
//            case "DELETE" -> "DELETE";
//            default -> "UNKNOWN";
//        };
//    }
}
