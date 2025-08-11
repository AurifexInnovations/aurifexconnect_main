package com.erp.Multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Resolves the current tenant identifier from the thread-local context.
 * If no tenant is explicitly set, it falls back to the default "public" schema.
 */
@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_TENANT = "public";
    private static final Logger logger = LoggerFactory.getLogger(CurrentTenantIdentifierResolverImpl.class);

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = TenantContext.getCurrentTenant();

        if (tenant == null || tenant.trim().isEmpty()) {
            logger.debug("No tenant found in context. Falling back to default tenant: {}", DEFAULT_TENANT);
            return DEFAULT_TENANT;
        }

        logger.debug("Resolved current tenant identifier: {}", tenant);
        return tenant;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        // Returning true allows Hibernate to reuse existing sessions if the tenant hasn't changed.
        return true;
    }
}
