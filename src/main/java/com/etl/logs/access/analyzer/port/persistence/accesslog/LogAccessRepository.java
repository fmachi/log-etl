package com.etl.logs.access.analyzer.port.persistence.accesslog;

import com.etl.logs.access.analyzer.domain.accesslog.Access;
import com.etl.logs.access.analyzer.domain.accesslog.ExceedingTrafficIp;

import java.util.List;

public interface LogAccessRepository {

    void cleanPreviousElaboration();

    void insertLogRows(List<Access> logLinesBatch);
    
    List<ExceedingTrafficIp> findIpsToBlacklist(ExceedingTrafficCriteria criteria);
}
