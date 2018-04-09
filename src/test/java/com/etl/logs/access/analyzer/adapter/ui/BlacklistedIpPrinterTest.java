package com.etl.logs.access.analyzer.adapter.ui;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistedIp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BlacklistedIpPrinterTest {

    BlacklistedIpPrinter blacklistedIpPrinter;
    @Mock
    private PrintStream printStream;
    @Captor
    private ArgumentCaptor<String> captor;

    @Before
    public void setup() {
        blacklistedIpPrinter = new BlacklistedIpPrinter(printStream);
    }

    @Test
    public void shouldPrintBlacklistedIp() {
        LocalDateTime startDate = LocalDateTime.of(2018,4,3,11,12,22);
        BlacklistedIp blacklistedIp = BlacklistedIp.builder()
                .ip("192.168.25.4")
                .startDate(startDate)
                .endDate(startDate.plusDays(1))
                .message("message")
                .numberOfAccess(1)
                .build();

        blacklistedIpPrinter.accept(blacklistedIp);

        verify(printStream).println(captor.capture());

        assertEquals("Found ip to blacklist BlacklistedIp(ip=192.168.25.4, startDate=2018-04-03T11:12:22, endDate=2018-04-04T11:12:22, numberOfAccess=1, message=message)",captor.getValue());

    }

}