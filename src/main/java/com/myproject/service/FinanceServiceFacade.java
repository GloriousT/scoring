package com.myproject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static java.math.MathContext.DECIMAL64;

@Slf4j
@AllArgsConstructor
public class FinanceServiceFacade {

    private final YFinanceService yFinanceService;
    private final MacroTrendsFinanceService macroTrendsFinanceService;

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
        return yFinanceService.getPriceChange();
    }

    public BigInteger getEarningsChange() {
        return macroTrendsFinanceService.getEarningsChange();
    }
}
