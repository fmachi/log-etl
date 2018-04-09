package com.etl.logs.access.analyzer.adapter.ui;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistedIp;

import java.io.PrintStream;
import java.util.function.Consumer;

public class BlacklistedIpPrinter implements Consumer<BlacklistedIp>{

    private final PrintStream out;

    public BlacklistedIpPrinter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void accept(BlacklistedIp blacklistedIp) {
        out.println("Found ip to blacklist "+blacklistedIp);
    }
}
