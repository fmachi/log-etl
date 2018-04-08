package com.etl.logs.access.analyzer.adapter.persistence.accesslog.blacklisting;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistedIp;
import com.etl.logs.access.analyzer.port.persistence.blacklisting.BlacklistingRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcTemplateBlacklistingRepository implements BlacklistingRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateBlacklistingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void clearPreviousElaboration() {

    }

    @Override
    public void blacklistIps(List<BlacklistedIp> toBlacklist) {

    }
}
