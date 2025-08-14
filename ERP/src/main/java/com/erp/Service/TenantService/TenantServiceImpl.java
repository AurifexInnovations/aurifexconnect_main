package com.erp.Service.TenantService;

import com.erp.Config.ConnectionPoolManager;
import com.erp.Exception.Tenant.TenantNotFound;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final JdbcTemplate jdbcTemplate;
    private final ConnectionPoolManager connectionPoolManager;

    public void deleteTenant(String schemaName) {
        // 1️⃣ Check if schema exists
        Boolean schemaExists = jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM information_schema.schemata WHERE schema_name = ?)",
                Boolean.class,
                schemaName
        );

        if (Boolean.FALSE.equals(schemaExists)) {
            throw new TenantNotFound("Tenant schema not found: " + schemaName);
        }

        // 2️⃣ Drop schema
        jdbcTemplate.execute("DROP SCHEMA " + schemaName + " CASCADE");

        // 3️⃣ Evict stale connections
        connectionPoolManager.evictConnections();
    }

}

