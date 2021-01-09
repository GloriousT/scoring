package com.myproject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
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
        var ratio = priceChange
                .divide(earningsChange, DECIMAL64)
                .setScale(2, RoundingMode.CEILING);
        log.info("Ratio price to earnings: {}", ratio);
        return ratio;
    }

    public BigDecimal getPriceChange() {
        return yFinanceService.getPriceChange();
    }

    public BigDecimal getEarningsChange() {
        return macroTrendsFinanceService.getEarningsChange();
    }

    public int getNumberSignificantYoYEpsFalls() {
        return macroTrendsFinanceService.getNumberSignificantYoYEpsFalls();
    }

    public BigDecimal getTrailingPe() {
        return macroTrendsFinanceService.getPriceToEarnings10YearsAverage();
    }

    public BigDecimal getInterestCoverage() {
        return yFinanceService.getInterestCoverageRatio();
    }
}
