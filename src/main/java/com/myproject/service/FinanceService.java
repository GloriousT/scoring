package com.myproject.service;

import com.myproject.client.MacroTrendsClient;
import com.myproject.client.YFinanceClient;
import com.myproject.dto.MacrotrendsQuarterlyEarnings;
import com.myproject.dto.PriceChartV8Dto;
import io.restassured.path.xml.element.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import static io.restassured.mapper.ObjectMapperType.GSON;
import static java.math.MathContext.DECIMAL64;

@Slf4j
@AllArgsConstructor
public class FinanceService {

    private final YFinanceClient financeClient;
    private final MacroTrendsClient macroTrendsClient;

    public BigDecimal getPriceGrowthToEarningsGrowthRatio() {
        var priceChange = getPriceChange();
        var earningsChange = getEarningsChange();
        var ratio = new BigDecimal(priceChange)
                .divide(new BigDecimal(earningsChange), DECIMAL64)
                .setScale(2, RoundingMode.CEILING);
        log.info("Ratio price to earnings: {}", ratio);
        return ratio;
    }

    public BigInteger getPriceChange() {
        var price = financeClient.get10YearsPriceHistory()
                .statusCode(200)
                .extract().as(PriceChartV8Dto.class, GSON);
        var priceChange = price.get10YearsPriceChange();
        log.info("Price change is {}%:", priceChange);
        return priceChange;
    }

    public BigInteger getEarningsChange() {
        var document = macroTrendsClient.getEarningsHistory()
                .statusCode(200)
                .extract().htmlPath();
        Node earnings = (Node) document.getList("**.findAll { it.@class == 'historical_data_table table' }").get(1);
        if (!earnings.children().get(0).toString().contains("Quarterly EPS")) {
            throw new RuntimeException("Can't read EPS for " + financeClient);
        }
        var quarterlyEarnings = MacrotrendsQuarterlyEarnings.from(earnings.children().get(1));
        var earningsChange = quarterlyEarnings.get10YearsEpsChange();
        log.info("Earnings change is {}%:", earningsChange);
        return earningsChange;
    }

}
