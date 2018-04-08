package com.etl.logs.access.analyzer.adapter.ui;

import com.etl.logs.access.analyzer.domain.ui.InputParameters;
import com.etl.logs.access.analyzer.port.ui.InputParametersParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.util.Optional;

@Slf4j
public class CLIInputParametersParser implements InputParametersParser {

    private static final String START_DATE = "startDate";
    private static final String DURATION = "duration";
    private static final String THRESHOLD = "threshold";
    private static final String START_DATE_DESCRIPTION = "it's the moment from which we will calculate the number of access";
    private static final String DURATION_DESCRIPTION = "it's the duration of the period we are considering and can be hourly or daily";
    private static final String THRESHOLD_DESCRIPTION = "it's the number of access after which the ip must be blocked";
    private static final String ERROR = "An error occurred parsing command line parameters, please check the log and the instructions.";


    private final Options options;
    private final CommandLineParser parser;

    public CLIInputParametersParser() {
        this.options = initCommandLineOptions();
        this.parser = new DefaultParser();
    }

    @Override
    public Optional<InputParameters> parse(String[] args) {
        try {
            return innerParseParameters(args);
        } catch (ParseException ex) {
            log.error("An error occurred parsing command line parameters", ex);
            return handleError();
        }
    }

    private Optional<InputParameters> innerParseParameters(String[] args) throws ParseException {
        CommandLine line = parser.parse(options, args);

        InputParameters inputParameters = extractInputParameters(line);

        return inputParameters.checkAreValid()
                .map(this::handleValidationError)
                .orElse(
                        Optional.of(inputParameters)
                );
    }

    private Optional<InputParameters> handleValidationError(String validationError) {
        log.error("An error occurred parsing input parameters: {}", validationError);
        return handleError();
    }

    private InputParameters extractInputParameters(CommandLine line) {
        boolean startDateProvided = line.hasOption(START_DATE);
        boolean durationProvided = line.hasOption(DURATION);
        boolean thresholdProvided = line.hasOption(THRESHOLD);

        InputParameters.InputParametersBuilder builder = InputParameters.builder();
        if (startDateProvided) {
            String optionValue = line.getOptionValue(START_DATE);
            builder.startDate(optionValue);
            log.info("Start date is {}", optionValue);
        }
        if (durationProvided) {
            String optionValue = line.getOptionValue(DURATION);
            builder.duration(optionValue);
            log.info("Duration is {}", optionValue);
        }
        if (thresholdProvided) {
            String optionValue = line.getOptionValue(THRESHOLD);
            builder.threshold(optionValue);
            log.info("Threshold is {}", optionValue);
        }

        return builder.build();
    }

    private Optional<InputParameters> handleError() {
        System.err.println(ERROR);
        showHelp(options);
        return Optional.empty();
    }

    private Options initCommandLineOptions() {
        Options options = new Options();
        options.addOption(createOption(START_DATE, START_DATE_DESCRIPTION));
        options.addOption(createOption(DURATION, DURATION_DESCRIPTION));
        options.addOption(createOption(THRESHOLD, THRESHOLD_DESCRIPTION));
        return options;
    }

    private Option createOption(String optionName, String optionDescription) {
        return Option.builder()
                .argName(optionName)
                .longOpt(optionName)
                .desc(optionDescription)
                .hasArg(true)
                .required()
                .build();
    }

    private static void showHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("parser", options);
    }

}
