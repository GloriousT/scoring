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
        try {
            return yFinanceService.getPriceChange();
        } catch (Exception e) {
            log.error("Exception when counting 10y price change", e);
            return null;
        }
    }

    public BigDecimal getEarningsChange() {
        return macroTrendsFinanceService.getEarningsChange();
    }

    public int getNumberSignificantYoYEpsFalls() {
        return macroTrendsFinanceService.getNumberSignificantYoYEpsFalls();
    }

    public BigDecimal getTrailingPe() {
        try {
            return macroTrendsFinanceService.getPriceToEarnings10YearsAverage();
        } catch (Exception e) {
            log.error("Exception when counting trailing PE", e);
            return null;
        }
    }

    public BigDecimal getInterestCoverage() {
        return yFinanceService.getInterestCoverageRatio();
    }

    public BigDecimal getPB() {
        try {
            return yFinanceService.getPb();
        } catch (Exception e) {
            log.error("Exception when counting PB", e);
            return null;
        }
    }

    public BigDecimal getGrahamCriteria() {
        var grahamCriteria = getTrailingPe().multiply(getPB());
        log.info("Graham criteria: {}", grahamCriteria);
        return grahamCriteria;
    }

    private Boolean isNegative10YearTrailingNetIncomePresent() {
        try {
            return macroTrendsFinanceService.isNegative10YearTrailingNetIncomePresent();
        } catch (Exception e) {
            log.error("Exception when counting isNegative10YearTrailingNetIncomePresent", e);
            return null;
        }
    }

    private Boolean hasPositiveEBITDA() {
        try {
            return yFinanceService.hasPositiveEBITDA();
        } catch (Exception e) {
            log.error("Exception when counting hasPositiveEBITDA", e);
            return null;
        }
    }

    public BigDecimal getTotalLiabilitiesToCurrentAssetsRatio() {
        try {
            return yFinanceService.getTotalLiabilitiesToCurrentAssetsRatio();
        } catch (Exception e) {
            log.error("Exception when counting getTotalLiabilitiesToCurrentAssetsRatio", e);
            return null;
        }
    }

    public BigDecimal getLongTermDebtToTotalAssetsRatio() {
        try {
            return yFinanceService.getLongTermDebtToTotalAssetsRatio();
        } catch (Exception e) {
            log.error("Exception when counting getLongTermDebtToTotalAssetsRatio", e);
            return null;
        }
    }

    public BigDecimal getDebtToEquityRatio() {
        try {
            return yFinanceService.getDebtToEquityRatio();
        } catch (Exception e) {
            log.error("Exception when counting getDebtToEquityRatio", e);
            return null;
        }
    }

    public BigDecimal getQuickRatio() {
        try {
            return yFinanceService.getQuickRatio();
        } catch (Exception e) {
            log.error("Exception when counting getQuickRatio", e);
            return null;
        }
    }

    public Integer getYearsOfDivsPaid() {
        try {
            return yFinanceService.getYearsOfDivsPaid();
        } catch (Exception e) {
            log.error("Exception when counting divs paid", e);
            return null;
        }
    }

    public BigDecimal getDpsGrowth() {
        try {
            return yFinanceService.getDpsGrowth();
        } catch (Exception e) {
            log.error("Exception when counting divs paid", e);
            return null;
        }
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

            var negative10YearTrailingNetIncomePresent = isNegative10YearTrailingNetIncomePresent();
            partialEvaluation.negative10YearTrailingNetIncomePresent(negative10YearTrailingNetIncomePresent);

            var trailing10YearsEarningsChangeInPercent = getEarningsChange();
            partialEvaluation.trailing10YearsEarningsChangeInPercent(trailing10YearsEarningsChangeInPercent);

            var significantYearOverYearEpsFallings = getNumberSignificantYoYEpsFalls();
            partialEvaluation.significantYearOverYearEpsFallings(significantYearOverYearEpsFallings);

            var hasPositiveEBITDA = hasPositiveEBITDA();
            partialEvaluation.positiveEBITDA(hasPositiveEBITDA);

            var interestCoverage = getInterestCoverage();
            partialEvaluation.interestCoverage(interestCoverage);

            var totalLiabilitiesToCurrentAssetsRatio = getTotalLiabilitiesToCurrentAssetsRatio();
            partialEvaluation.totalLiabilitiesToCurrentAssetsRatio(totalLiabilitiesToCurrentAssetsRatio);

            var longTermDebtToTotalAssetsRatio = getLongTermDebtToTotalAssetsRatio();
            partialEvaluation.longTermDebtToTotalAssetsRatio(longTermDebtToTotalAssetsRatio);

            var debtToEquityRatio = getDebtToEquityRatio();
            partialEvaluation.debtToEquityRatio(debtToEquityRatio);

            var quickRatio = getQuickRatio();
            partialEvaluation.quickRatio(quickRatio);

            var yearsOfDivsPaid = getYearsOfDivsPaid();
            partialEvaluation.yearsOfDivsPaid(yearsOfDivsPaid);

            var growingDps = getDpsGrowth();
            partialEvaluation.growingDps(growingDps);
        } catch (Exception e) {
            log.error("Exception when calculating full evaluation", e);
            return partialEvaluation.build();
        }
        return partialEvaluation.build();
    }
}
