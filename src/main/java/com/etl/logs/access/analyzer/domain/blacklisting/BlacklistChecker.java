package com.etl.logs.access.analyzer.domain.blacklisting;

import com.etl.logs.access.analyzer.domain.ui.InputParameters;
import com.etl.logs.access.analyzer.port.persistence.accesslog.ExceedingTrafficCriteria;
import com.etl.logs.access.analyzer.port.persistence.accesslog.LogAccessRepository;
import com.etl.logs.access.analyzer.port.persistence.blacklisting.BlacklistingRepository;
import com.etl.logs.access.analyzer.port.ui.ExceedingTrafficIpPrinter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

@Slf4j
public class BlacklistChecker {

    private final LogAccessRepository logAccessRepository;
    private final BlacklistingRepository blacklistingRepository;
    private BiFunction<ExceedingTrafficIp, ExceedingTrafficCriteria, String> messageFormatter;
    private ExceedingTrafficIpPrinter exceedingTrafficIpPrinter;

    public BlacklistChecker(LogAccessRepository logAccessRepository,
                            BlacklistingRepository blacklistingRepository,
                            BiFunction<ExceedingTrafficIp, ExceedingTrafficCriteria, String> messageFormatter,
                            ExceedingTrafficIpPrinter exceedingTrafficIpPrinter) {
        this.logAccessRepository = logAccessRepository;
        this.blacklistingRepository = blacklistingRepository;
        this.exceedingTrafficIpPrinter = exceedingTrafficIpPrinter;
        this.messageFormatter = messageFormatter;
    }

    public void checkAndBlacklistIps(InputParameters inputParameters) {
        ExceedingTrafficCriteria trafficCriteria = ExceedingTrafficCriteria.builder()
                .lowerBound(inputParameters.getStartDate())
                .maxNumberOfAccess(inputParameters.getThreshold())
                .upperBound(inputParameters.getEndDate())
                .build();

        List<ExceedingTrafficIp> ipsToBlacklist = logAccessRepository.findIpsToBlacklist(trafficCriteria);

        if(!ipsToBlacklist.isEmpty()) {
            blacklistItems(trafficCriteria, ipsToBlacklist);
        } else {
            exceedingTrafficIpPrinter.noResult();
        }
    }

    private void blacklistItems(ExceedingTrafficCriteria trafficCriteria, List<ExceedingTrafficIp> ipsToBlacklist) {
        log.info("Found {} rows to blacklist", ipsToBlacklist.size());

        List<BlacklistedIp> blacklistedIpList = ipsToBlacklist.stream().map(
                ip -> toBlackListedIp(ip, trafficCriteria)
        ).collect(toList());

        int numberOfWrittenRows = blacklistingRepository.insertBlacklistedIps(blacklistedIpList);
        log.info("Number of blacklisted rows persisted is {}", numberOfWrittenRows);
    }

    private BlacklistedIp toBlackListedIp(ExceedingTrafficIp ipsToBlacklist, ExceedingTrafficCriteria trafficCriteria) {
        BlacklistedIp toBlackList = BlacklistedIp.builder()
                .ip(ipsToBlacklist.getIpAddress())
                .startDate(trafficCriteria.getLowerBound())
                .endDate(trafficCriteria.getUpperBound())
                .numberOfAccess(ipsToBlacklist.getNumberOfaccess())
                .message(createMessage(ipsToBlacklist,trafficCriteria))
                .build();

        exceedingTrafficIpPrinter.onBlacklistedIpFound(toBlackList);

        return toBlackList;
    }

    private String createMessage(ExceedingTrafficIp ipsToBlacklist,
                                 ExceedingTrafficCriteria trafficCriteria) {
        return messageFormatter.apply(ipsToBlacklist,trafficCriteria);
    }
}
