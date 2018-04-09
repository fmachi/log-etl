package com.etl.logs.access.analyzer.domain.blacklisting;

import com.etl.logs.access.analyzer.port.persistence.accesslog.ExceedingTrafficCriteria;
import com.etl.logs.access.analyzer.port.persistence.accesslog.LogAccessRepository;
import com.etl.logs.access.analyzer.port.persistence.blacklisting.BlacklistingRepository;
import com.etl.logs.access.analyzer.port.ui.ExceedingTrafficIpPrinter;
import org.junit.Before;

import java.util.function.BiFunction;

import static org.junit.Assert.*;

public class BlacklistCheckerTest {

    BlacklistChecker blacklistChecker;
    private LogAccessRepository logAccessRepository;
    private BlacklistingRepository blacklistingRepository;
    private BiFunction<ExceedingTrafficIp, ExceedingTrafficCriteria, String> messageFormatter;
    private ExceedingTrafficIpPrinter exceedingTrafficIpPrinter;

    @Before
    public void setup() {
        blacklistChecker = new BlacklistChecker(
                                    logAccessRepository,
                                    blacklistingRepository,
                                    messageFormatter,
                                    exceedingTrafficIpPrinter
                            );
    }

}