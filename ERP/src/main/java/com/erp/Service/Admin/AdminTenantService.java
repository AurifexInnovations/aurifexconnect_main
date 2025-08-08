package com.erp.Service.Admin;

import com.erp.Dto.Request.AdminRequest;
import com.erp.Model.Admin;
import com.erp.Multitenancy.TenantContext;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminTenantService {
    private static final Logger logger = LoggerFactory.getLogger(AdminTenantService.class);
    private final AdminPersistenceService adminPersistenceService;

    public Admin saveAdminInTenantSchema(AdminRequest request, String schemaName) {
        logger.info("Preparing to save admin in tenant schema: {}", schemaName);
        TenantContext.setCurrentTenant(schemaName); // Set tenant context
        try {
            return adminPersistenceService.saveAdminInSchema(request, schemaName);
        } finally {
            TenantContext.clear(); // Always clear tenant context
            logger.debug("Cleared tenant context after operation");
        }
    }
}