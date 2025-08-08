package com.erp.Multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContextHolder implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(TenantContextHolder.class);
    private final String previousTenant;

    public TenantContextHolder(String tenantId) {
        this.previousTenant = TenantContext.getCurrentTenant(); // read from TenantContext
        TenantContext.setCurrentTenant(tenantId); // delegate to TenantContext
        logger.debug("Switched to tenant: {}", tenantId);
    }

    @Override
    public void close() {
        if (previousTenant != null) {
            TenantContext.setCurrentTenant(previousTenant);
            logger.debug("Restored tenant context to: {}", previousTenant);
        } else {
            TenantContext.clear();
            logger.debug("Cleared tenant context");
        }
    }
}