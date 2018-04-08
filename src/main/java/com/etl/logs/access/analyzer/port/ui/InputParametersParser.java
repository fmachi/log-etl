package com.etl.logs.access.analyzer.port.parameters;

import com.etl.logs.access.analyzer.domain.InputParameters;

import java.util.Optional;

public interface InputParametersParser {

    Optional<InputParameters> parse(String[] args);
}
