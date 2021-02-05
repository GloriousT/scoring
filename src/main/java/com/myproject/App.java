package com.myproject;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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
    // Negative 10 year trailing net income
    // Diluted EPS growth 10 year trailing - done!
    // Diluted EPS grows faster than price 10 years trailing - done!
    // EPS falls > 5% 10 years trailing - done!
    // Positive EBITDA
    // Interest coverage - done!
    // Total liabilities / current assets
    // Long term debt / total assets
    // Debt / Equity
    // Quick ratio
    // Div paid years
    // Growing DPS stability
    // 3 years trailing div growth

    @SneakyThrows
    public static void main(String[] args) {
        var finances = financeService("QCOM");
        finances.getInterestCoverage();
//        var fullEvaluation = finances.getFullEvaluation();
//        System.out.println(">>>>>>>>>>>>>>>>>");
//        System.out.println(fullEvaluation.toString());
//        fullEvaluation.printExcelSet();
//        var finances = financeService("FB");
//        finances.getEarningsChange();
//        var priceChange = finances.getPriceChange();
//        var earningsChange = finances.getEarningsChange();
//        var priceGrowthToEarningsGrowthRatio = finances.getPriceGrowthToEarningsGrowthRatio();
//        var trailingPe = finances.getTrailingPe();
//        var numberSignificantYoYEpsFalls= finances.getNumberSignificantYoYEpsFalls();
//        System.out.println("results==================================================");
//        System.out.println(priceChange);
//        System.out.println(earningsChange);
//        System.out.println(priceGrowthToEarningsGrowthRatio);
//        System.out.println(trailingPe);
//        System.out.println(numberSignificantYoYEpsFalls);
//        System.out.println(finances.getInterestCoverage());
//        System.out.println(finances.getPB());
//        System.out.println(finances.getPB());
//        System.out.println(finances.getPB());
//        System.out.println(finances.getPB());
    }
}
