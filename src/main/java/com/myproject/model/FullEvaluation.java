package com.myproject.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public class FullEvaluation {
    BigDecimal trailing10YearPE;
    BigDecimal trailing10YearPriceGrowthInPercent;
    BigDecimal priceToBook;
    boolean negative10YearTrailingNetIncomePresent;
    BigDecimal trailing10YearsEarningsChangeInPercent;
    int significantYearOverYearEpsFallings;
    boolean positiveEBITDA;
    BigDecimal interestCoverage;
    BigDecimal totalLiabilitiesToCurrentAssetsRatio;
    BigDecimal longTermDebtToTotalAssetsRatio;
    BigDecimal debtToEquityRatio;
    BigDecimal quickRatio;
    int yearsOfDivsPaid;
    boolean growingDps;
}
