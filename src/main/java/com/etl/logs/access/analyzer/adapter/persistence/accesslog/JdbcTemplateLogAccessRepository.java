package com.etl.logs.access.analyzer.adapter.accesslog;

import com.etl.logs.access.analyzer.domain.accesslog.Access;
import com.etl.logs.access.analyzer.domain.accesslog.ExceedingTrafficIp;
import com.etl.logs.access.analyzer.port.persistence.accesslog.ExceedingTrafficCriteria;
import com.etl.logs.access.analyzer.port.persistence.accesslog.LogAccessRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Types.INTEGER;
import static java.sql.Types.TIMESTAMP;

@Slf4j
public class JdbcTemplateLogAccessRepository implements LogAccessRepository {

    private static final String INSERT_LOG_ACCESS_ROWS_BATCH = "INSERT INTO `logdatabase`.`ACCESS` " +
            " (`TIMESTAMP`,`IP_ADDRESS`,`METHOD`,`STATUS`,`USER_AGENT`) " +
            " VALUES (?,?,?,?, ?);";
    ;
    private static final String SELECT_IP_EXCEEDING_TRAFFIC =
            "select a.IP_ADDRESS,COUNT(*) as NUMBER_OF_ACCESS from ACCESS a " +
                    " WHERE a.timestamp between ? and ? " +
                    " group by IP_ADDRESS having NUMBER_OF_ACCESS>? "+
                    " order by NUMBER_OF_ACCESS desc";

    final JdbcTemplate jdbcTemplate;

    public JdbcTemplateLogAccessRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void clearPreviousElaboration() {
        int numberOfDeletedRows = jdbcTemplate.update("DELETE from ACCESS");
        log.info("Removed {} rows", numberOfDeletedRows);
    }

    @Override
    public void insertLogRows(List<Access> logLinesBatch) {
        int[][] ints = jdbcTemplate.batchUpdate(
                INSERT_LOG_ACCESS_ROWS_BATCH,
                logLinesBatch,
                logLinesBatch.size(),
                (preparedStatement, access) -> {
                    int index = 1;
                    preparedStatement.setString(index++, access.getTimestamp());
                    preparedStatement.setString(index++, access.getIp());
                    preparedStatement.setString(index++, access.getMethod());
                    preparedStatement.setString(index++, access.getStatus());
                    preparedStatement.setString(index++, access.getUserAgent());
                }
        );
    }

    @Override
    public List<ExceedingTrafficIp> findIpsToBlacklist(ExceedingTrafficCriteria criteria) {
        return jdbcTemplate.query(SELECT_IP_EXCEEDING_TRAFFIC,
                new Object[]{criteria.getLowerBound(), criteria.getUpperBound(), criteria.getMaxNumberOfAccess()},
                new int[]{TIMESTAMP, TIMESTAMP, INTEGER},
                (rs, rowNum) -> ExceedingTrafficIp.builder()
                        .ipAddress(rs.getString(1))
                        .numberOfaccess(rs.getInt(2))
                        .build()

        );
    }
}
