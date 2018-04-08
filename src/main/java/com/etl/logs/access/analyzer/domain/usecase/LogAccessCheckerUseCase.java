package com.etl.logs.access.analyzer.domain.usecase;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistChecker;
import com.etl.logs.access.analyzer.domain.feeder.AccessLogFeeder;
import com.etl.logs.access.analyzer.domain.persistence.DatabaseCleaner;
import com.etl.logs.access.analyzer.domain.ui.InputParameters;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogAccessCheckerUseCase {

    private final DatabaseCleaner databaseCleaner;
    private final AccessLogFeeder accessLogFeeder;
    private final BlacklistChecker blacklistChecker;

    public LogAccessCheckerUseCase(DatabaseCleaner databaseCleaner, AccessLogFeeder accessLogFeeder, BlacklistChecker blacklistChecker) {
        this.databaseCleaner = databaseCleaner;
        this.accessLogFeeder = accessLogFeeder;
        this.blacklistChecker = blacklistChecker;
    }

    public void process(InputParameters inputParameters) {
        log.info("Cleaning database");
        databaseCleaner.cleanDatabase();

        log.info("Loading access log feed");
        accessLogFeeder.feed();

        log.info("Checking ips, blacklisting lawbreakers");
        blacklistChecker.checkAndBlacklistIps(inputParameters);
    }
}
