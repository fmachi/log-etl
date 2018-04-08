package com.etl.logs.access.analyzer.domain.blacklisting;

import com.etl.logs.access.analyzer.domain.accesslog.ExceedingTrafficIp;
import com.etl.logs.access.analyzer.domain.ui.InputParameters;
import com.etl.logs.access.analyzer.port.persistence.accesslog.ExceedingTrafficCriteria;
import com.etl.logs.access.analyzer.port.persistence.accesslog.LogAccessRepository;
import com.etl.logs.access.analyzer.port.persistence.blacklisting.BlacklistingRepository;

import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class BlacklistChecker {

    private final LogAccessRepository logAccessRepository;
    private final BlacklistingRepository blacklistingRepository;
    private Consumer<BlacklistedIp> blacklistedIpFoundHandler;

    public BlacklistChecker(LogAccessRepository logAccessRepository,
                            BlacklistingRepository blacklistingRepository,
                            Consumer<BlacklistedIp> blacklistedIpFoundHandler) {
        this.logAccessRepository = logAccessRepository;
        this.blacklistingRepository = blacklistingRepository;
        this.blacklistedIpFoundHandler = blacklistedIpFoundHandler;
    }

    public void checkAndBlacklistIps(InputParameters inputParameters) {
        ExceedingTrafficCriteria trafficCriteria = ExceedingTrafficCriteria.builder()
                .lowerBound(inputParameters.getStartDate())
                .maxNumberOfAccess(inputParameters.getThreshold())
                .upperBound(inputParameters.getEndDate())
                .build();

        List<ExceedingTrafficIp> ipsToBlacklist = logAccessRepository.findIpsToBlacklist(trafficCriteria);

        List<BlacklistedIp> blacklistedIpList = ipsToBlacklist.stream().map(
                ip -> toBlackListedIp(ip, trafficCriteria)
        ).collect(toList());

        blacklistingRepository.insertBlacklistedIps(blacklistedIpList);
    }

    private BlacklistedIp toBlackListedIp(ExceedingTrafficIp ipsToBlacklist, ExceedingTrafficCriteria trafficCriteria) {
        BlacklistedIp toBlackList = BlacklistedIp.builder()
                .ip(ipsToBlacklist.getIpAddress())
                .startDate(trafficCriteria.getLowerBound())
                .endDate(trafficCriteria.getUpperBound())
                .numberOfAccess(ipsToBlacklist.getNumberOfaccess())
                .message("Exceeded allowed number of access")
                .build();

        blacklistedIpFoundHandler.accept(toBlackList);

        return toBlackList;
    }
}
