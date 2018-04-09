package com.etl.logs.access.analyzer.adapter.ui;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistedIp;
import com.etl.logs.access.analyzer.port.ui.ExceedingTrafficIpPrinter;

import java.io.PrintStream;
import java.util.function.Consumer;

public class BlacklistedIpPrinter implements ExceedingTrafficIpPrinter {

    private final PrintStream out;

    public BlacklistedIpPrinter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void onBlacklistedIpFound(BlacklistedIp blacklistedIp) {
        out.println("Found ip to blacklist "+blacklistedIp);
    }

    @Override
    public void noResult() {
        out.println("No ip to blacklist found for the given criteria.");
    }

}
