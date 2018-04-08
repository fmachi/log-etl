package com.etl.logs.access.analyzer.domain.blacklisting;

import com.etl.logs.access.analyzer.domain.accesslog.ExceedingTrafficIp;
import com.etl.logs.access.analyzer.port.persistence.accesslog.ExceedingTrafficCriteria;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.BiFunction;

public class ExceedingTrafficMessageFormatter implements BiFunction<ExceedingTrafficIp, ExceedingTrafficCriteria, String> {
    @Override
    public String apply(ExceedingTrafficIp ipsToBlacklist, ExceedingTrafficCriteria exceedingTrafficCriteria) {
        return String.format("The ip '%s' has been blocked because it performed %d access from '%s' to '%s'.",
                ipsToBlacklist.getIpAddress(),
                ipsToBlacklist.getNumberOfaccess(),
                toString(exceedingTrafficCriteria.getLowerBound()),
                toString(exceedingTrafficCriteria.getUpperBound())
        );
    }

    private Object toString(LocalDateTime value) {
        return Optional.ofNullable(value).map(LocalDateTime::toString).orElse("null");
    }
}
