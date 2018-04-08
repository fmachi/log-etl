package com.etl.logs.access.analyzer;

import com.etl.logs.access.analyzer.domain.ui.InputParameters;
import com.etl.logs.access.analyzer.domain.usecase.LogAccessCheckerUseCase;
import com.etl.logs.access.analyzer.port.ui.InputParametersParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.etl.logs.access.analyzer")
@Import(ApplicationConfiguration.class)
@Slf4j
public class ApplicationLauncher implements CommandLineRunner {


    @Autowired
    InputParametersParser inputParametersParser;

    @Autowired
    LogAccessCheckerUseCase logAccessCheckerUseCase;


    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationLauncher.class, args);
    }

    @Override
    public void run(String... params) throws Exception {
        inputParametersParser.parse(params).ifPresent(this::processAccessLogFile);
    }


    private void processAccessLogFile(InputParameters inputParameters) {
        logAccessCheckerUseCase.process(inputParameters);
    }
}
