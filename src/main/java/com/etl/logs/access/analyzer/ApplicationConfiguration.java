package com.zopa.exercise.ratecalculator;

import com.zopa.exercise.ratecalculator.application.CSVBasedRateCalculationSystemFactory;
import com.zopa.exercise.ratecalculator.application.LoanQuoteApplication;
import com.zopa.exercise.ratecalculator.engine.MortgageCalculator;
import com.zopa.exercise.ratecalculator.persistence.validation.CSVLineValidator;
import com.zopa.exercise.ratecalculator.presentation.Console;
import com.zopa.exercise.ratecalculator.presentation.STDOUTConsole;
import com.zopa.exercise.ratecalculator.validation.AmountValidator;
import com.zopa.exercise.ratecalculator.validation.FileNameValidator;
import com.zopa.exercise.ratecalculator.validation.InputParametersValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Value("${rate-calculator.engine.years}")
    Integer years;

    @Value("${rate-calculator.engine.compounding}")
    Integer compounding;

    @Bean
    LoanQuoteApplication loanQuoteApplication(InputParametersValidator validator, Console console, CSVBasedRateCalculationSystemFactory csvBasedRateCalculationSystemFactory) {
        return new LoanQuoteApplication(
                validator,
                console,
                csvBasedRateCalculationSystemFactory,
                years,
                compounding
        );
    }

    @Bean
    CSVBasedRateCalculationSystemFactory calculationSystemFactory(MortgageCalculator mortgageCalculator, CSVLineValidator csvLineValidator) {
        return new CSVBasedRateCalculationSystemFactory(mortgageCalculator,csvLineValidator);
    }

    @Bean
    CSVLineValidator csvLineValidator() {
        return new CSVLineValidator();
    }

    @Bean
    MortgageCalculator mortgageCalculator() {
        return new MortgageCalculator();
    }


    @Bean
    InputParametersValidator inputParametersValidator() {
        return new InputParametersValidator(
                new FileNameValidator(),
                new AmountValidator()
        );
    }

    @Bean
    String noQuotationPossibleMessage() {
        return "No quotation found";
    }

    @Bean
    String[] getInstructions() {
        return new String[]{
                "RateCalculationSystem Usage ",
                "You can run this application with the following command: ",
                "cmd> ratecalculator-1.0-SNAPSHOT.jar [market-file] [loan-amount]",
                " if any offer is found, the output will be:",
                "     Requested amount: £XXXX  ",
                "     Rate: X.X%  ",
                "     Monthly repayment: £XXXX.XX  ",
                "     Total repayment: £XXXX.XX ",
                " if no offer is found the application output will be: ",
                "     No quotation possible"
        };
    }

    @Bean
    STDOUTConsole console(String[] instructions,
                          String noQuotationPossibleMessage) {
        return new STDOUTConsole(
                instructions,
                noQuotationPossibleMessage,
                System.out::println,
                System.err::println
        );
    }

}
