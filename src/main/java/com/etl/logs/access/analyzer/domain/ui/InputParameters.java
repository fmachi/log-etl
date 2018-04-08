package com.etl.logs.access.analyzer.domain.ui;

import com.etl.logs.access.analyzer.domain.Duration;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@ToString
public class InputParameters {

    static DateTimeFormatter START_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'.'HH:mm:ss");

    static final String WRONG_DURATION = "Duration parameter can be set only to <hourly> or <daily>.";
    static final String WRONG_THRESHOLD_FORMAT = "Please provide a valid threshold value.";
    static final String NOT_POSITIVE_THRESHOLD = "Please provide a positive threshold.";
    static final String START_DATE_INT_THE_FUTURE = "Please provide a startDate in the past.";
    static final String WRONG_START_DATE_FORMAT = "Please provide a valid startDate with the following format <yyyy-MM-dd'.'HH:mm:ss>.";

    String startDate;
    String duration;
    String threshold;

    @Builder
    public InputParameters(String startDate, String duration, String threshold) {
        this.startDate = startDate;
        this.duration = duration;
        this.threshold = threshold;

        convertValuesQuietly();
    }


    private LocalDateTime startDateAsLocalDateTime;
    private Duration durationAsEnum;
    private Integer thresholdAsInteger;

    public Optional<String> checkAreValid() {
        return Optional.ofNullable(validate());
    }

    public Duration getDuration() {
        return durationAsEnum;
    }

    public LocalDateTime getStartDate() {
        return startDateAsLocalDateTime;
    }

    public int getThreshold() {
        return Optional.ofNullable(thresholdAsInteger).orElse(0);
    }

    private void convertValuesQuietly() {
        extractStartDate();
        extractDuration();
        extractThreshold();
    }

    private void extractThreshold() {
        try {
            thresholdAsInteger = Integer.valueOf(threshold).intValue();
        } catch (Exception ex) {
            log.error("Unable to parse threshold {}.",duration);
            log.error("Exception: ",ex);
        }
    }

    private void extractDuration() {
        String nullSafeUpperCaseDuration = Optional.ofNullable(duration).map(String::toUpperCase).orElse("");

        try {
            durationAsEnum = Duration.valueOf(nullSafeUpperCaseDuration);
        } catch (Exception ex) {
            log.error("Unable to parse duration {}.",duration);
            log.error("Exception: ",ex);
        }
    }

    private void extractStartDate() {
        try {
            startDateAsLocalDateTime = parseStartDate();
        } catch (Exception ex) {
            log.error("Unable to parse startDate {}.",startDate);
            log.error("Exception: ",ex);
        }
    }


    private String validate() {
        if (!isValidDuration()) {
            return WRONG_DURATION;
        }
        if (!isValidStartDate()) {
            return WRONG_START_DATE_FORMAT;
        }

        if (isStartDateInTheFuture()) {
            return START_DATE_INT_THE_FUTURE;
        }

        if(!isThresholdAValidInteger()) {
            return WRONG_THRESHOLD_FORMAT;
        }
        if(isNotPositiveThreshold()) {
            return NOT_POSITIVE_THRESHOLD;
        }

        return null;
    }

    private boolean isValidStartDate() {
        return startDateAsLocalDateTime!=null;
    }

    private LocalDateTime parseStartDate() {
        return LocalDateTime.parse(startDate, START_DATE_FORMATTER);
    }

    private  boolean isStartDateInTheFuture() {
        return !startDateAsLocalDateTime.isBefore(LocalDateTime.now());
    }

    private  boolean isValidDuration() {
        return durationAsEnum!=null;
    }

    private  boolean isThresholdAValidInteger() {
        return thresholdAsInteger!=null;
    }

    private  boolean isNotPositiveThreshold() {
        return thresholdAsInteger<=0;
    }

    public LocalDateTime getEndDate() {
        // TODO avoid nullpointer
        if(durationAsEnum.isDaily())
            return startDateAsLocalDateTime.plusDays(1);
        else
            return startDateAsLocalDateTime.plusHours(1);
    }
}
