package com.myproject;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.myproject.service.ServiceProvider.financeService;


@Slf4j
public class App {

    //todo:
    // price - excel
    // market cap - excel
    // 10 year trailing PE - done!
    // 10 year price growth - done!
    // current PE - excel
    // current PB - done!
    // Graham criteria
    // Negative 10 year trailing net income - done!
    // Diluted EPS growth 10 year trailing - done!
    // Diluted EPS grows faster than price 10 years trailing - done!
    // EPS falls > 5% 10 years trailing - done!
    // Positive EBITDA - done!
    // Interest coverage - done!
    // Total liabilities / current assets - done!
    // Long term debt / total assets - done!
    // Debt / Equity - done!
    // Quick ratio - done!
    // Div paid years - done!
    // Growing DPS stability
    // 3 years trailing div growth - done!

    public static void main(String[] args) {
        Stream.of("CMCSA", "JKHY", "PFE", "RTX", "NOC", "LMT", "ADP")
                .map(it -> financeService(it).getFullEvaluation().toGoogleSheetSplit())
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }
}
