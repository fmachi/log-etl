package com.etl.logs.access.analyzer.domain.blacklisting;

import com.etl.logs.access.analyzer.port.persistence.accesslog.ExceedingTrafficCriteria;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class ExceedingTrafficMessageFormatterTest {

    ExceedingTrafficMessageFormatter formatter = new ExceedingTrafficMessageFormatter();

    @Test
    public void shouldFormatExceedingTrafficMessage() {

        ExceedingTrafficIp ipToBlacklist = ExceedingTrafficIp.builder()
                .numberOfaccess(200)
                .ipAddress("192.168.25.61")
                .build();
        ExceedingTrafficCriteria trafficCriteria = ExceedingTrafficCriteria.builder()
                .lowerBound(LocalDateTime.of(2018,1,1,13,12,25))
                .upperBound(LocalDateTime.of(2018,1,1,14,12,25))
                .build();

        String formatMessage = formatter.apply(ipToBlacklist, trafficCriteria);

        assertEquals("The ip '192.168.25.61' has been blocked because it performed 200 access from '2018-01-01T13:12:25' to '2018-01-01T14:12:25'.",formatMessage);
    }

}