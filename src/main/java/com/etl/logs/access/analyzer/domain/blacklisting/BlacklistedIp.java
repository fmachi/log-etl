package com.etl.logs.access.analyzer.domain.blacklisting;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class BlacklistedIp {

    String ip;
    LocalDateTime startDate;
    LocalDateTime endDate;
    long numberOfAccess;
    String message;
}
