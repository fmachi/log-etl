package com.etl.logs.access.analyzer.port.persistence.blacklisting;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistedIp;

import java.util.List;

public interface BlacklistingRepository {

    void cleanPreviousElaboration();

    void insertBlacklistedIps(List<BlacklistedIp> toBlacklist);
}
