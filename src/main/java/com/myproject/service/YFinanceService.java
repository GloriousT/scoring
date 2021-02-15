package com.myproject.service;

import com.myproject.client.YFinanceClient;
import com.myproject.dto.yahoo.price.v8.DividendEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;

import static java.math.BigDecimal.ZERO;

@Slf4j
@AllArgsConstructor
public class YFinanceService {

    private final YFinanceClient financeClient;

    public BigDecimal getPriceChange() {
        var price = financeClient.get10YearsPriceHistory();
        var priceChange = price.get10YearsPriceChange();
        log.info("Price change is {}%:", priceChange.multiply(BigDecimal.valueOf(100)));
        return priceChange;
    }

    public BigDecimal getInterestCoverageRatio() {
        var incomeStatement = financeClient.getIncomeStatement();
        var interestCoverage = incomeStatement.getInterestCoverage();
        log.info("Interest coverage ratio is {}:", interestCoverage);
        return interestCoverage;
    }

    public BigDecimal getPb() {
        var stats = financeClient.getKeyStatistics();
        var pb = stats.getPB();
        log.info("PB is {}%:", pb);
        return pb;
    }

    public Boolean hasPositiveEBITDA() {
        var ebitda = financeClient.getFinancialData().getEBITDA();
        log.info("EBITDA TTM is: {}", ebitda);
        return ebitda.compareTo(ZERO) > 0;
    }

    public BigDecimal getTotalLiabilitiesToCurrentAssetsRatio() {
        var balanceSheet = financeClient.getBalanceSheet();
        var totalLiabilities = balanceSheet.getLatestTotalLiabilities();
        var currentAssets = balanceSheet.getLatestCurrentAssets();
        var ratio = totalLiabilities.divide(currentAssets, MathContext.DECIMAL64)
                .setScale(2, RoundingMode.CEILING);
        log.info("Total Liabilities to Current Assets Ratio is: {}", ratio);
        return ratio;
    }

    public BigDecimal getLongTermDebtToTotalAssetsRatio() {
        var balanceSheet = financeClient.getBalanceSheet();
        var longTermDebt = balanceSheet.getLongTermDebt();
        var totalAssets = balanceSheet.getTotalAssets();
        var ratio = longTermDebt.divide(totalAssets, MathContext.DECIMAL64)
                .setScale(2, RoundingMode.CEILING);
        log.info("Long term debt to Total Assets Ratio is: {}", ratio);
        return ratio;
    }

    public BigDecimal getDebtToEquityRatio() {
        var balanceSheet = financeClient.getBalanceSheet();
        var debt = balanceSheet.getLongTermDebt();
        var equity = balanceSheet.getEquity();
        var ratio = debt.divide(equity, MathContext.DECIMAL64)
                .setScale(2, RoundingMode.CEILING);
        log.info("Debt to Equity ratio is: {}", ratio);
        return ratio;
    }

    public BigDecimal getQuickRatio() {
        var financialData = financeClient.getFinancialData();
        var quickRatio = financialData.getQuickRatio()
                .setScale(2, RoundingMode.CEILING);
        log.info("Quick ratio is: {}", quickRatio);
        return quickRatio;
    }

    public Integer getYearsOfDivsPaid() {
        var divHistory = financeClient.getDividendHistory();
        //assuming divs paid quarterly
        var yearsDivs = divHistory.getOrderedDividends().size() / 4;
        log.info("Years of dividend paid is not less than: {}", yearsDivs);
        return yearsDivs;
    }

    public BigDecimal getDpsGrowth() {
        var divHistory = financeClient.getDividendHistory();
        //assuming divs paid quarterly
        var orderedDivs = divHistory.getOrderedDividends();
        Collections.reverse(orderedDivs);
        var lastYear = orderedDivs.stream()
                .filter(it -> orderedDivs.indexOf(it) < 4)
                .map(DividendEvent::getAmount)
                .reduce(BigDecimal::add)
                .get();
        var threeYearsBefore = orderedDivs.stream()
                .filter(it -> {
                    int currentIndex = orderedDivs.indexOf(it);
                    return currentIndex > 11 && currentIndex < 16;
                })
                .map(DividendEvent::getAmount)
                .reduce(BigDecimal::add)
                .get();
        var dpsGrowth = lastYear.subtract(threeYearsBefore).divide(threeYearsBefore, MathContext.DECIMAL64)
                .setScale(4, RoundingMode.CEILING);
        log.info("3 years dps growth is: {}%", dpsGrowth.multiply(BigDecimal.valueOf(100)));
        return dpsGrowth;
    }
}
