package com.etl.logs.access.analyzer.domain;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.etl.logs.access.analyzer.domain.InputParameters.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputParametersTest {


    @Test
    public void shouldFailValidationForMonthlyDuration() {
        InputParameters inputParameters = InputParameters.builder()
                .duration("monthly")
                .build();

        Optional<String> errors = inputParameters.checkAreValid();

        checkErrorsContain(errors, WRONG_DURATION);

    }

    @Test
    public void shouldFailValidationForWrongDateFormat() {
        InputParameters inputParameters = InputParameters.builder()
                .duration("daily")
                .startDate("2018-01-01 12:22:23")
                .build();

        Optional<String> errors = inputParameters.checkAreValid();

        checkErrorsContain(errors, WRONG_START_DATE_FORMAT);
    }

    @Test
    public void shouldFailValidationForDateInTheFuture() {
        InputParameters inputParameters = InputParameters.builder()
                .duration("daily")
                .startDate(dateInTheFuture())
                .build();

        Optional<String> errors = inputParameters.checkAreValid();

        checkErrorsContain(errors, START_DATE_INT_THE_FUTURE);
    }

    @Test
    public void shouldFailValidationForWrongThreshold() {
        InputParameters inputParameters = InputParameters.builder()
                .duration("daily")
                .startDate("2017-01-01.13:00:00")
                .threshold("aaa")
                .build();

        Optional<String> errors = inputParameters.checkAreValid();

        checkErrorsContain(errors, WRONG_THRESHOLD_FORMAT);
    }

    @Test
    public void shouldFailValidationForNegativeThreshold() {
        InputParameters inputParameters = InputParameters.builder()
                .duration("daily")
                .startDate("2017-01-01.13:00:00")
                .threshold("-4")
                .build();

        Optional<String> errors = inputParameters.checkAreValid();

        checkErrorsContain(errors, NOT_POSITIVE_THRESHOLD);
    }

    @Test
    public void shouldFailValidationForZeroThreshold() {
        InputParameters inputParameters = InputParameters.builder()
                .duration("daily")
                .startDate("2017-01-01.13:00:00")
                .threshold("0")
                .build();

        Optional<String> errors = inputParameters.checkAreValid();

        checkErrorsContain(errors, NOT_POSITIVE_THRESHOLD);
    }

    @Test
    public void shouldValidateSuccessfully() {
        InputParameters inputParameters = InputParameters.builder()
                .duration("daily")
                .startDate("2017-01-01.13:00:00")
                .threshold("200")
                .build();

        Optional<String> errors = inputParameters.checkAreValid();

        assertFalse(errors.isPresent());
    }

    @Test
    public void shouldConvertFields() {
        InputParameters inputParameters = InputParameters.builder()
                .duration("daily")
                .startDate("2017-01-01.13:12:58")
                .threshold("200")
                .build();

        assertEquals(Duration.DAILY,inputParameters.getDuration());
        assertEquals(LocalDateTime.of(2017,1,1,13,12,58),inputParameters.getStartDate());
        assertEquals(200,inputParameters.getThreshold());
    }

    private String dateInTheFuture() {
        return LocalDateTime.now().plusDays(2).format(START_DATE_FORMATTER);
    }

    private void checkErrorsContain(Optional<String> errors, String expectedError) {
        assertTrue("Expected validation error", errors.isPresent());
        assertEquals("Wrong error message", expectedError, errors.get());
    }

}