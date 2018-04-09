package com.etl.logs.access.analyzer.adapter.ui;

import com.etl.logs.access.analyzer.domain.Duration;
import com.etl.logs.access.analyzer.domain.ui.InputParameters;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;

public class CLIInputParametersParserTest {

    private final LocalDateTime startDate = LocalDateTime.of(2017, 1, 1, 13, 12, 29);
    CLIInputParametersParser parser = new CLIInputParametersParser();

    @Test
    public void shouldParseInputParametersHourly(){
        String[] inputParameters = new String[] {
                "--startDate=2017-01-01.13:12:29",
                "--duration=hourly",
                "--threshold=180"
        };

        Optional<InputParameters> parsedParameters = parser.parse(inputParameters);

        checkParsedParameters(parsedParameters, startDate, startDate.plusHours(1), Duration.HOURLY, 180);
    }

    @Test
    public void shouldParseInputParametersDaily(){
        String[] inputParameters = new String[] {
                "--startDate=2017-01-01.13:12:29",
                "--duration=daily",
                "--threshold=250"
        };

        Optional<InputParameters> parsedParameters = parser.parse(inputParameters);

        checkParsedParameters(parsedParameters, startDate, startDate.plusDays(1), Duration.DAILY, 250);
    }

    private void checkParsedParameters(Optional<InputParameters> parsedParameters, LocalDateTime startDate, LocalDateTime expected, Duration duration, int threshold) {
        assertTrue(parsedParameters.isPresent());
        InputParameters actual = parsedParameters.get();
        assertEquals(threshold, actual.getThreshold());
        assertEquals(duration, actual.getDuration());
        assertEquals(startDate, actual.getStartDate());
        assertEquals(expected, actual.getEndDate());
    }


}