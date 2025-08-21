package com.erp.Config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

/**
 * Manages the HikariCP connection pool for the application.
 * Provides methods to instantly clear (evict) all existing DB connections.
 * Useful after dropping tenant schemas or making DB changes to avoid stale connections.
 */
@Component
public class ConnectionPoolManager {

    private final HikariDataSource hikariDataSource;

    /**
     * Spring will auto-inject the HikariDataSource used by the app.
     */
    public ConnectionPoolManager(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    /**
     * Soft-evicts all current DB connections in the pool.
     * - Marks all connections as "evicted"
     * - Any active ones will finish their work and then close
     * - Next time a connection is requested, HikariCP will create a new one
     */
    public void evictConnections() {
        try {
            hikariDataSource.getHikariPoolMXBean().softEvictConnections();
            System.out.println("✅ All HikariCP connections evicted successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to evict HikariCP connections: " + e.getMessage());
            e.printStackTrace();
        }
    }
}