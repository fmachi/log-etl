package com.etl.logs.access.analyzer.adapter.persistence.blacklisting;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistedIp;
import com.etl.logs.access.analyzer.port.persistence.blacklisting.BlacklistingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class JdbcTemplateBlacklistingRepository implements BlacklistingRepository {

    private static final String INSERT_BLACKLISTED_ROWS = "INSERT INTO `BLACKLISTED_IPS` " +
            "(`IP_ADDRESS`, `START_DATE`,`END_DATE`,`NUM_ACCESS`,`MESSAGE`) " +
            " VALUES " +
            "(?, ?, ?, ?,?);";

    public static final String DELETE_BLACKLISTED_ROWS = "DELETE from BLACKLISTED_IPS";

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateBlacklistingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void cleanPreviousElaboration() {
        int numberOfDeletedRows = jdbcTemplate.update(DELETE_BLACKLISTED_ROWS);
        log.info("Removed {} rows", numberOfDeletedRows);
    }

    @Override
    public void insertBlacklistedIps(List<BlacklistedIp> toBlacklist) {
        int[][] ints = jdbcTemplate.batchUpdate(
                INSERT_BLACKLISTED_ROWS,
                toBlacklist,
                toBlacklist.size(),
                (preparedStatement, blacklistedIp) -> {
                    int index = 1;
                    preparedStatement.setString(index++, blacklistedIp.getIp());
                    preparedStatement.setTimestamp(index++, asTimestamp(blacklistedIp.getStartDate()));
                    preparedStatement.setTimestamp(index++, asTimestamp(blacklistedIp.getEndDate()));
                    preparedStatement.setLong(index++, blacklistedIp.getNumberOfAccess());
                    preparedStatement.setString(index++, blacklistedIp.getMessage());
                }
        );
    }

    private Timestamp asTimestamp(LocalDateTime date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(date.format(dtf));
    }
}
