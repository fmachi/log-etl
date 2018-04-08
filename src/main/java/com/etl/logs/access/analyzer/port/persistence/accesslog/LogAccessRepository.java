package com.etl.logs.access.analyzer.port.persistence;

import com.etl.logs.access.analyzer.domain.Access;
import com.etl.logs.access.analyzer.domain.ExceedingTrafficIp;

import java.util.List;

public interface LogAccessRepository {

    void clearPreviousElaboration();

    void loadLogBatch(List<Access> logLinesBatch);
    
    List<ExceedingTrafficIp> findIpsToBlacklist(ExceedingTrafficCriteria criteria);
}
