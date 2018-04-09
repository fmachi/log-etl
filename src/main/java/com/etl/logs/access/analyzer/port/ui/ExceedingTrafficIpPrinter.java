package com.etl.logs.access.analyzer.port.ui;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistedIp;

public interface ExceedingTrafficIpPrinter {

    void noResult();

    void onBlacklistedIpFound(BlacklistedIp blacklistedIp);
}
