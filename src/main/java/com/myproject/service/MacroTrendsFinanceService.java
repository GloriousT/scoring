package com.myproject.service;

import com.myproject.client.MacroTrendsClient;
import com.myproject.dto.MacrotrendsQuarterlyEarnings;
import io.restassured.path.xml.element.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@AllArgsConstructor
public class MacroTrendsFinanceService {
    private final MacroTrendsClient macroTrendsClient;

    public BigInteger getEarningsChange() {
        var document = macroTrendsClient.getEarningsHistory();
        Node earnings = (Node) document.getList("**.findAll { it.@class == 'historical_data_table table' }").get(1);
        if (!earnings.children().get(0).toString().contains("Quarterly EPS")) {
            throw new RuntimeException("Can't read EPS for " + macroTrendsClient);
        }
        var quarterlyEarnings = MacrotrendsQuarterlyEarnings.from(earnings.children().get(1));
        var earningsChange = quarterlyEarnings.get10YearsEpsChange();
        log.info("Earnings change is {}%:", earningsChange);
        return earningsChange;
    }

    public BigDecimal getPriceToEarnings10YearsAverage() {
        var priceToEarningsHistory = macroTrendsClient.getPriceToEarningsHistory();
        return BigDecimal.ONE;
    }
}
