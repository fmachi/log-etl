package com.etl.logs.access.analyzer.adapter.ui;

import com.etl.logs.access.analyzer.domain.blacklisting.BlacklistedIp;

import java.util.function.Consumer;

public class BlacklistedIpPrinter implements Consumer<BlacklistedIp>{
    @Override
    public void accept(BlacklistedIp blacklistedIp) {
        System.out.println("Found ip to blacklist "+blacklistedIp);
    }
}
