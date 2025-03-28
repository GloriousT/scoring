package com.myproject.service;

import com.myproject.client.MacroTrendsClient;
import com.myproject.dto.macrotrends.QuarterlyPriceRatios;
import io.restassured.path.xml.element.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
public class MacroTrendsFinanceService {
    private final MacroTrendsClient macroTrendsClient;

    public int getNumberSignificantYoYEpsFalls() {
        var annualEarnings = macroTrendsClient.getAnnualEarningsHistory();
        return annualEarnings.getEarningsFallsCountGreaterThan(5);
    }

    public BigDecimal getEarningsChange() {
        var quarterlyEarnings = macroTrendsClient.getQuarterlyEarningsHistory();
        var earningsChange = quarterlyEarnings.get10YearsEpsChange();
        log.info("Earnings change is {}%:", earningsChange.multiply(BigDecimal.valueOf(100)));
        return earningsChange;
    }

    public BigDecimal getPriceToEarnings10YearsAverage() {
        var document = macroTrendsClient.getPriceToEarningsHistory();
        var peHistoryNode = (Node) document.getList("**.findAll { it.@class == 'table' } ")
                .stream()
                .filter(it -> it.toString().contains("PE Ratio Historical Data"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find PE Ratio history"));
        var peHistoryItems = peHistoryNode.children().list()
                .stream()
                .filter(it -> "tbody".equals(it.name()))
                .findFirst().orElseThrow(() -> new RuntimeException("Can't find PE Ratio history items"))
                .children()
                .list()
                .subList(1, 41);
        var priceDynamics = QuarterlyPriceRatios.from(peHistoryItems);
        return priceDynamics.get10YearsTrailingPE();
    }

    public Boolean isNegative10YearTrailingNetIncomePresent() {
        var annualEarnings = macroTrendsClient.getAnnualEarningsHistory();
        return annualEarnings.isNegative10YearTrailingNetIncomePresent();
    }
}
