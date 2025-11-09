package com.bankingapplication.account_service.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHealthIndicator.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        try {
            // Execute a simple query to check database connectivity
            int result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            if (result == 1) {
                return Health.up()
                        .withDetail("database", "PostgreSQL")
                        .withDetail("status", "Connected")
                        .build();
            } else {
                return Health.down()
                        .withDetail("database", "PostgreSQL")
                        .withDetail("status", "Unexpected response")
                        .build();
            }
        } catch (Exception e) {
            log.error("Database health check failed", e);
            return Health.down()
                    .withDetail("database", "PostgreSQL")
                    .withDetail("status", "Disconnected")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
