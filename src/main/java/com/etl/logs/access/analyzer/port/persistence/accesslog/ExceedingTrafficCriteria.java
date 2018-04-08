package com.etl.logs.access.analyzer.port.persistence.accesslog;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ExceedingTrafficCriteria {
    LocalDateTime lowerBound;
    LocalDateTime upperBound;
    int maxNumberOfAccess;
}
