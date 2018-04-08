package com.zopa.exercise.ratecalculator;

import com.zopa.exercise.ratecalculator.application.LoanQuoteApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.zopa.exercise.ratecalculator")
@Import(ApplicationConfiguration.class)
public class ApplicationLauncher implements CommandLineRunner {

    @Autowired
    LoanQuoteApplication loanQuoteApplication;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationLauncher.class, args);
    }

    @Override
    public void run(String... params) throws Exception {
        loanQuoteApplication.run(params);
    }
}
