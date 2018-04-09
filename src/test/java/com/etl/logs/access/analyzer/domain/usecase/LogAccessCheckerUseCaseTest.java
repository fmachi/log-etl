package com.etl.logs.access.analyzer.domain.usecase;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistChecker;
import com.etl.logs.access.analyzer.domain.feeder.AccessLogFeeder;
import com.etl.logs.access.analyzer.domain.persistence.DatabaseCleaner;
import com.etl.logs.access.analyzer.domain.ui.InputParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LogAccessCheckerUseCaseTest {

    LogAccessCheckerUseCase logAccessCheckerUseCase;
    @Mock
    private DatabaseCleaner databaseCleaner;
    @Mock
    private AccessLogFeeder accessLogFeeder;
    @Mock
    private BlacklistChecker blacklistChecker;

    @Before
    public void setup() {
        logAccessCheckerUseCase = new LogAccessCheckerUseCase(databaseCleaner,accessLogFeeder,blacklistChecker);
    }

    @Test
    public void shouldOrchestrateInnerPhashes() {
        InputParameters inputParameters = InputParameters.builder().build();

        logAccessCheckerUseCase.process(inputParameters);

        verify(databaseCleaner).cleanDatabase();

        verify(accessLogFeeder).feed();

        verify(blacklistChecker).checkAndBlacklistIps(inputParameters);
    }

}