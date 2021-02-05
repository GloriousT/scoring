package com.myproject.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Builder
@Value
@Slf4j
public class FullEvaluation {
    String ticker;
    BigDecimal trailing10YearPE;
    BigDecimal trailing10YearPriceGrowthInPercent;
    BigDecimal priceToBook;
    Boolean negative10YearTrailingNetIncomePresent;
    BigDecimal trailing10YearsEarningsChangeInPercent;
    int significantYearOverYearEpsFallings;
    Boolean positiveEBITDA;
    BigDecimal interestCoverage;
    BigDecimal totalLiabilitiesToCurrentAssetsRatio;
    BigDecimal longTermDebtToTotalAssetsRatio;
    BigDecimal debtToEquityRatio;
    BigDecimal quickRatio;
    int yearsOfDivsPaid;
    Boolean growingDps;

    public void printExcelSet() {
        log.info("Excel for ticker: {}, Tr.PE:{}, prGrow:{}, PB:{}, negAnnEpsPresent:{} Tr.EPS-growth:{}, EPS f yoy:{}, posEbitda: {}, int.cov:{}",
                ticker,
                trailing10YearPE,
                trailing10YearPriceGrowthInPercent,
                priceToBook,
                negative10YearTrailingNetIncomePresent,
                trailing10YearsEarningsChangeInPercent,
                significantYearOverYearEpsFallings,
                positiveEBITDA,
                interestCoverage);
    }
}
