package com.etl.logs.access.analyzer.port.io;

import java.io.Closeable;

public interface LogLinesSource extends Iterable<String[]>, Closeable {

}
