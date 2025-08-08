package com.erp.Multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {

    // 🔒 Thread-safe storage for current tenant per request
    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();

    // 🌐 Default schema if none provided (fallback to public)
    private static final String DEFAULT_TENANT = "public";

    private static final Logger logger = LoggerFactory.getLogger(TenantContext.class);

    /**
     * ✅ Set the current tenant in context (used by filters or interceptors)
     * Falls back to 'public' schema if null or blank.
     */
    public static void setCurrentTenant(String tenantId) {
        if (tenantId == null || tenantId.trim().isEmpty()) {
            TENANT_ID.set(DEFAULT_TENANT);
            logger.debug("🔁 Tenant ID was null or empty, defaulted to: {}", DEFAULT_TENANT);
        } else {
            TENANT_ID.set(tenantId);
            logger.debug("✅ Tenant context set to: {}", tenantId);
        }
    }

    /**
     * ✅ Get the current tenant schema
     * If not set, return default 'public' schema.
     */
    public static String getCurrentTenant() {
        String tenantId = TENANT_ID.get();
        if (tenantId == null) {
            logger.debug("⏳ Tenant context not set, defaulting to: {}", DEFAULT_TENANT);
            return DEFAULT_TENANT;
        }
        return tenantId;
    }

    /**
     * ✅ Clear the tenant after each request to prevent leakage
     */
    public static void clear() {
        TENANT_ID.remove();
        logger.debug("🧹 Tenant context cleared");
    }
}
