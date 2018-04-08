package com.etl.logs.access.analyzer.port.persistence.accesslog.blacklisting;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistedIp;

import java.util.List;

public interface BlacklistingRepository {

    void blacklistIps(List<BlacklistedIp> toBlacklist);
}
