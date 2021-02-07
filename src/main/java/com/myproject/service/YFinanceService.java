package com.myproject.service;

import com.myproject.client.YFinanceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

@Slf4j
@AllArgsConstructor
public class YFinanceService {

    private final YFinanceClient financeClient;

    public BigDecimal getPriceChange() {
        var price = financeClient.get10YearsPriceHistory();
        var priceChange = price.get10YearsPriceChange();
        log.info("Price change is {}%:", priceChange);
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
}
