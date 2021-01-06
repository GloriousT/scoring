package com.myproject.service;

import com.myproject.client.MacroTrendsClient;
import com.myproject.dto.MacrotrendsQuarterlyEarnings;
import com.myproject.dto.MacrotrendsQuarterlyPriceRatios;
import io.restassured.path.xml.element.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;

@Slf4j
@AllArgsConstructor
public class MacroTrendsFinanceService {
    private final MacroTrendsClient macroTrendsClient;

    public BigInteger getEarningsChange() {
        var document = macroTrendsClient.getEarningsHistory();
        var earnings = (Node) document.getList("**.findAll { it.@class == 'historical_data_table table' }").get(1);
        if (!earnings.children().get(0).toString().contains("Quarterly EPS")) {
            throw new RuntimeException("Can't read EPS for " + macroTrendsClient);
        }
        var quarterlyEarnings = MacrotrendsQuarterlyEarnings.from(earnings.children().get(1));
        var earningsChange = quarterlyEarnings.get10YearsEpsChange();
        log.info("Earnings change is {}%:", earningsChange);
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
        var priceDynamics = MacrotrendsQuarterlyPriceRatios.from(peHistoryItems);
        return priceDynamics.get10YearsTrailingPE();
    }
}
