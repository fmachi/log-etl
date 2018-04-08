package com.etl.logs.access.analyzer.domain.accesslog;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ExceedingTrafficIp {
    String ipAddress;
    int numberOfaccess;
}
