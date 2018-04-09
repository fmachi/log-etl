package com.etl.logs.access.analyzer;

import com.etl.logs.access.analyzer.adapter.ui.BlacklistedIpPrinter;
import com.etl.logs.access.analyzer.adapter.ui.CLIInputParametersParser;
import com.etl.logs.access.analyzer.adapter.persistence.accesslog.JdbcTemplateLogAccessRepository;
import com.etl.logs.access.analyzer.adapter.io.BufferedReaderLogLinesSource;
import com.etl.logs.access.analyzer.adapter.persistence.blacklisting.JdbcTemplateBlacklistingRepository;
import com.etl.logs.access.analyzer.domain.blacklisting.ExceedingTrafficIp;
import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistChecker;
import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistedIp;
import com.etl.logs.access.analyzer.domain.blacklisting.ExceedingTrafficMessageFormatter;
import com.etl.logs.access.analyzer.domain.feeder.AccessLogFeeder;
import com.etl.logs.access.analyzer.domain.feeder.PipelineConfiguration;
import com.etl.logs.access.analyzer.domain.persistence.DatabaseCleaner;
import com.etl.logs.access.analyzer.domain.usecase.LogAccessCheckerUseCase;
import com.etl.logs.access.analyzer.port.io.LogLinesSource;
import com.etl.logs.access.analyzer.port.persistence.accesslog.ExceedingTrafficCriteria;
import com.etl.logs.access.analyzer.port.persistence.accesslog.LogAccessRepository;
import com.etl.logs.access.analyzer.port.persistence.blacklisting.BlacklistingRepository;
import com.etl.logs.access.analyzer.port.ui.ExceedingTrafficIpPrinter;
import com.etl.logs.access.analyzer.port.ui.InputParametersParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.function.BiFunction;
import java.util.function.Consumer;

@Configuration
public class ApplicationConfiguration {


    @Value("${log-etl.pipeline.bufferSize}")
    private int bufferSize;
    @Value("${log-etl.pipeline.parallelism}")
    private int parallelism;
    @Value("${log-etl.pipeline.prefetchSize}")
    private int prefetchSize;

    @Bean
    public InputParametersParser inputParametersParser() {
        return new CLIInputParametersParser();
    }

    @Bean
    public LogAccessCheckerUseCase logAccessCheckerUseCase(DatabaseCleaner databaseCleaner, AccessLogFeeder accessLogFeeder, BlacklistChecker blacklistChecker) {
        return new LogAccessCheckerUseCase(databaseCleaner, accessLogFeeder, blacklistChecker);
    }

    @Bean
    public AccessLogFeeder accessLogFeeder(LogLinesSource logLinesSource, LogAccessRepository logAccessRepository, PipelineConfiguration pipelineConfiguration) {
        return new AccessLogFeeder(logLinesSource, logAccessRepository, pipelineConfiguration);
    }

    @Bean
    BiFunction<ExceedingTrafficIp, ExceedingTrafficCriteria, String> messageFormatter() {
        return new ExceedingTrafficMessageFormatter();
    }

    @Bean
    PipelineConfiguration pipelineConfiguration() {
        return PipelineConfiguration.builder()
                .bufferSize(bufferSize)
                .parallelism(parallelism)
                .prefetchSize(prefetchSize)
                .build();
    }

    @Bean
    BlacklistChecker blacklistChecker(LogAccessRepository logAccessRepository, BlacklistingRepository blacklistingRepository, ExceedingTrafficIpPrinter blacklistedIpPrinter, BiFunction<ExceedingTrafficIp, ExceedingTrafficCriteria, String> messageFormatter) {
        return new BlacklistChecker(logAccessRepository, blacklistingRepository, messageFormatter, blacklistedIpPrinter);
    }

    @Bean
    DatabaseCleaner databaseCleaner(LogAccessRepository logAccessRepository, BlacklistingRepository blacklistingRepository) {
        return new DatabaseCleaner(logAccessRepository, blacklistingRepository);
    }

    @Bean
    public LogLinesSource logLinesSource() {
        return new BufferedReaderLogLinesSource("./logsample/access.log", "\\|");
    }

    @Bean
    public LogAccessRepository logAccessRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateLogAccessRepository(jdbcTemplate);
    }

    @Bean
    public BlacklistingRepository blacklistingRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateBlacklistingRepository(jdbcTemplate);
    }

    @Bean
    ExceedingTrafficIpPrinter blacklistedIpPrinter() {
        return new BlacklistedIpPrinter(System.out);
    }


}
