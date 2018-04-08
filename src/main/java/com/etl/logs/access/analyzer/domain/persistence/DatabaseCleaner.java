package com.etl.logs.access.analyzer.domain.persistence;

import com.etl.logs.access.analyzer.port.persistence.accesslog.LogAccessRepository;
import com.etl.logs.access.analyzer.port.persistence.blacklisting.BlacklistingRepository;

public class DatabaseCleaner {

    private final LogAccessRepository logAccessRepository;
    private final BlacklistingRepository blacklistingRepository;

    public DatabaseCleaner(LogAccessRepository logAccessRepository, BlacklistingRepository blacklistingRepository) {
        this.logAccessRepository = logAccessRepository;
        this.blacklistingRepository = blacklistingRepository;
    }

    public void cleanDatabase() {
        logAccessRepository.cleanPreviousElaboration();

        blacklistingRepository.cleanPreviousElaboration();
    }
}
