package com.etl.logs.access.analyzer.port.ui;

import com.etl.logs.access.analyzer.domain.ui.InputParameters;

import java.util.Optional;

public interface InputParametersParser {

    Optional<InputParameters> parse(String[] args);
}
