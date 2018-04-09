package com.etl.logs.access.analyzer.adapter.io;

import com.etl.logs.access.analyzer.port.io.ReadingLogLinesException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BufferedReaderLogLinesSourceTest {

    public static final String LOG_ENTRY_SEPARATOR = "\\|";
    public static final String TEST_FILE = "./src/test/resources/reader/access.log";
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    String[][] expectedTokens = {
            {"2017-01-01 00:00:11.763","192.168.234.82", "\"GET / HTTP/1.1\"","200","\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\""},
            {"2017-01-01 00:00:21.164","192.168.234.82","\"GET / HTTP/1.1\"","200","\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\""},
            {"2017-01-01 00:00:23.003","192.168.169.194","\"GET / HTTP/1.1\"","200","\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393\""}
    };

    @Mock
    private BufferedReader mockedBufferedReader;


    @Test
    public void shouldThrowExceptionWhenOpeningANotExistingFile() {
        thrown.expect(ReadingLogLinesException.class);
        thrown.expectMessage("An error occurred opening file <notExistingFile>.");
        new BufferedReaderLogLinesSource("notExistingFile", "whatever");
    }

    @Test
    public void shouldInvokeCloseOnTheUnderlyingBufferedReader() throws IOException {
        TestableBufferedReaderLogLinesSource bufferedReaderLogLinesSource = new TestableBufferedReaderLogLinesSource();


        bufferedReaderLogLinesSource.close();

        Mockito.verify(mockedBufferedReader).close();
    }

    @Test
    public void shouldIterateOverFileLines() throws IOException {
        BufferedReaderLogLinesSource bufferedReaderLogLinesSource = givenAValidBufferedReader();

        Iterator<String[]> iterator = bufferedReaderLogLinesSource.iterator();

        assertNotNull("Expecting to retrieve not null iterator", iterator);

        for(String[] expectedToken:expectedTokens) {
            assertTrue(iterator.hasNext());
            String[] values = iterator.next();
            assertEquals(expectedToken.length,values.length);
            for (int i = 0; i <expectedToken.length; i++) {
                assertEquals(expectedToken[i],values[i]);
            }
        }

        bufferedReaderLogLinesSource.close();

    }

    private BufferedReaderLogLinesSource givenAValidBufferedReader() {
        return new BufferedReaderLogLinesSource(TEST_FILE, LOG_ENTRY_SEPARATOR);
    }

    class TestableBufferedReaderLogLinesSource extends BufferedReaderLogLinesSource {

        public TestableBufferedReaderLogLinesSource() {
            super(TEST_FILE, LOG_ENTRY_SEPARATOR);
        }

        @Override
        protected BufferedReader createBufferedReader(String filePath) throws FileNotFoundException {
            return mockedBufferedReader;
        }
    }
}