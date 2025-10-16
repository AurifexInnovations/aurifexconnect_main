package com.erp.Service.Admin;

import com.erp.Dto.Request.AdminRequest;
import com.erp.Exception.Admin.AdminAlreadyExistsException;
import com.erp.Mapper.Admin.AdminMapper;
import com.erp.Model.Admin;
import com.erp.Multitenancy.TenantContext;
import com.erp.Security.util.UserIdentity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AdminPersistenceService {
    private static final Logger logger = LoggerFactory.getLogger(AdminPersistenceService.class);
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserIdentity userIdentity;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Admin saveAdminInSchema(AdminRequest request, String schemaName) {
        logger.info("Saving admin in tenant schema: {}", schemaName);
        logger.debug("Current tenant context: {}", TenantContext.getCurrentTenant());

        entityManager.unwrap(org.hibernate.Session.class).doWork(connection -> {
            connection.setSchema(schemaName);
            logger.debug("Manually set connection schema to: {}", schemaName);
        });

        entityManager.clear();

        Query query = entityManager.createQuery(
                "SELECT COUNT(a) FROM Admin a WHERE a.email = :email", Long.class);
        query.setParameter("email", request.getEmail());
        Long count = (Long) query.getSingleResult();
        if (count > 0) {
            logger.warn("Admin already exists in schema {}: {}", schemaName, request.getEmail());
            throw new AdminAlreadyExistsException("Admin already exists: " + request.getEmail());
        }

        Admin admin = adminMapper.mapToAdmin(request);
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setCreatedByRootUserId(userIdentity.getCurrentUser().getId());
        admin.setLastUpdatedByRootUserId(userIdentity.getCurrentUser().getId());
        admin.setSchemaName(schemaName);

        entityManager.persist(admin);
        entityManager.flush();
        logger.info("Admin saved successfully in schema: {}", schemaName);
        return admin;
    }
}
