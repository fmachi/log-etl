package com.etl.logs.access.analyzer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Access {
    String timestamp;
    String ip;
    String method;
    String status;
    String userAgent;
}
