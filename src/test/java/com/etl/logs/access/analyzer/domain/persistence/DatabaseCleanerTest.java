package com.etl.logs.access.analyzer.domain.persistence;

import com.etl.logs.access.analyzer.port.persistence.accesslog.LogAccessRepository;
import com.etl.logs.access.analyzer.port.persistence.blacklisting.BlacklistingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseCleanerTest {

    DatabaseCleaner databaseCleaner;
    @Mock
    private LogAccessRepository logAccessRepository;
    @Mock
    private BlacklistingRepository blacklistingRepository;

    @Before
    public void setup() {
        databaseCleaner = new DatabaseCleaner(logAccessRepository, blacklistingRepository);
    }

    @Test
    public void shouldCleanupAllDatabases() {
        databaseCleaner.cleanDatabase();

        Mockito.verify(logAccessRepository).cleanPreviousElaboration();

        Mockito.verify(blacklistingRepository).cleanPreviousElaboration();

    }

}