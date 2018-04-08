package com.etl.logs.access.analyzer.domain;

public enum Duration {
    HOURLY,
    DAILY;

    public boolean isDaily() {
        return DAILY.equals(this);
    }

    public boolean isHourly() {
        return HOURLY.equals(this);
    }
}
