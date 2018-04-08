package com.etl.logs.access.analyzer.domain.blacklisting;

import com.etl.logs.access.analyzer.domain.accesslog.ExceedingTrafficIp;
import com.etl.logs.access.analyzer.port.persistence.accesslog.ExceedingTrafficCriteria;

import java.util.function.BiFunction;

public class MessageFormatter implements BiFunction<ExceedingTrafficIp,ExceedingTrafficCriteria,String>{
    @Override
    public String apply(ExceedingTrafficIp ipsToBlacklist, ExceedingTrafficCriteria exceedingTrafficCriteria) {
        return String.format("The ip '%s' has been blocked because it performed %d access from '%t' to '%t'.",
                ipsToBlacklist.getIpAddress(),
                ipsToBlacklist.getNumberOfaccess(),
                exceedingTrafficCriteria.getLowerBound(),
                exceedingTrafficCriteria.getUpperBound());
    }
}
