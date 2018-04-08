package com.etl.logs.access.analyzer.port.io;

public class ReadingLogLinesException extends RuntimeException {
    public ReadingLogLinesException(String message) {
        super(message);
    }

    public ReadingLogLinesException(String message, Throwable cause) {
        super(message, cause);
    }
}
