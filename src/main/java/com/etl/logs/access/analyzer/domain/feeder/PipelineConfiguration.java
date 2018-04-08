package com.etl.logs.access.analyzer.domain.feeder;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PipelineConfiguration {

    private final int parallelism;
    private final int bufferSize;
    private final int prefetchSize;

}
