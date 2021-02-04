package com.myproject.service;

import com.myproject.model.FullEvaluation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.MathContext.DECIMAL64;

@Slf4j
@AllArgsConstructor
public class FinanceServiceFacade {

    private final String ticker;
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

    public BigDecimal getPB() {
        return yFinanceService.getPb();
    }

    public BigDecimal getGrahamCriteria() {
        var grahamCriteria = getTrailingPe().multiply(getPB());
        log.info("Graham criteria: {}", grahamCriteria);
        return grahamCriteria;
    }

    public FullEvaluation getFullEvaluation() {
        var partialEvaluation = FullEvaluation.builder()
                .ticker(ticker);
        try {
            var trailingPe = getTrailingPe();
            partialEvaluation.trailing10YearPE(trailingPe);

            var trailingPriceGrowth = getPriceChange();
            partialEvaluation.trailing10YearPriceGrowthInPercent(trailingPriceGrowth);

            var priceToBook = getPB();
            partialEvaluation.priceToBook(priceToBook);

            //todo
            partialEvaluation.negative10YearTrailingNetIncomePresent(null);

            var trailing10YearsEarningsChangeInPercent = getEarningsChange();
            partialEvaluation.trailing10YearsEarningsChangeInPercent(trailing10YearsEarningsChangeInPercent);

            var significantYearOverYearEpsFallings = getNumberSignificantYoYEpsFalls();
            partialEvaluation.significantYearOverYearEpsFallings(significantYearOverYearEpsFallings);

            //todo
            partialEvaluation.positiveEBITDA(null);

            var interestCoverage = getInterestCoverage();
            partialEvaluation.interestCoverage(interestCoverage);

//            BigDecimal totalLiabilitiesToCurrentAssetsRatio;
//            BigDecimal longTermDebtToTotalAssetsRatio;
//            BigDecimal debtToEquityRatio;
//            BigDecimal quickRatio;
//            int yearsOfDivsPaid;
//            Boolean growingDps;
        } catch (Exception e) {
            log.error("Exception when calculating full evaluation", e);
            return partialEvaluation.build();
        }
        return partialEvaluation.build();
    }
}
