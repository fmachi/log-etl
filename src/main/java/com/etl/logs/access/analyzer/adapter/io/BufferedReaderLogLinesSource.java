package com.etl.logs.access.analyzer.adapter.io;

import com.etl.logs.access.analyzer.port.io.LogLinesSource;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@Slf4j
public class BufferedReaderLogLinesSource implements LogLinesSource {


    private final String logEntrySeparator;
    private final BufferedReader bufferedReader;

    public BufferedReaderLogLinesSource(String filePath,String logEntrySeparator) {
        this.logEntrySeparator = logEntrySeparator;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred opening file " + filePath, ex);
        }
    }

    @Override
    public void close() throws IOException {
        log.info("Closing buffered stream of log lines");
        bufferedReader.close();
    }

    @Override
    public Iterator<String[]> iterator() {
        return new Iterator<String[]>() {
            public String line;

            @Override
            public boolean hasNext() {
                try {
                    line = bufferedReader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException("Error reading log line", ex);
                }
                return line != null;
            }

            @Override
            public String[] next() {
                return Optional.ofNullable(line)
                        .map(line -> line.split(logEntrySeparator))
                        .orElse(null);
            }
        };
    }
}
