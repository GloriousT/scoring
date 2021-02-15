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
    Integer yearsOfDivsPaid;
    BigDecimal growingDps;

    public void printExcelSet() {
        log.info("Excel for ticker: {}, Tr.PE:{}, prGrow:{}, PB:{}, negAnnEpsPresent:{} Tr.EPS-growth:{}, EPS f yoy:{}, posEbitda: {}, int.cov:{}," +
                        "totLia/CurrAss: {}, lngTrmDebt/TotAss: {}, dbt/eq: {}, quickR: {}, yrsDivs: {}, growing dps: {}",
                ticker,
                trailing10YearPE,
                trailing10YearPriceGrowthInPercent,
                priceToBook,
                negative10YearTrailingNetIncomePresent,
                trailing10YearsEarningsChangeInPercent,
                significantYearOverYearEpsFallings,
                positiveEBITDA,
                interestCoverage,
                totalLiabilitiesToCurrentAssetsRatio,
                longTermDebtToTotalAssetsRatio,
                debtToEquityRatio,
                quickRatio,
                yearsOfDivsPaid,
                growingDps);
    }

    public String toGoogleSheetSplit() {
        return new StringBuilder()
                .append("=SPLIT(\"")
                .append(ticker).append(",")
                .append(" , , , ,")
                .append(trailing10YearPE).append(",")
                .append(trailing10YearPriceGrowthInPercent).append(",")
                .append(" , ,")
                .append(priceToBook).append(",")
                .append(" ,")
                .append(negative10YearTrailingNetIncomePresent).append(",")
                .append(trailing10YearsEarningsChangeInPercent).append(",")
                .append(" ,")
                .append(significantYearOverYearEpsFallings).append(",")
                .append(positiveEBITDA).append(",")
                .append(interestCoverage).append(",")
                .append(totalLiabilitiesToCurrentAssetsRatio).append(",")
                .append(longTermDebtToTotalAssetsRatio).append(",")
                .append(debtToEquityRatio).append(",")
                .append(quickRatio).append(",")
                .append(yearsOfDivsPaid).append(",")
                .append(" ,")
                .append(growingDps)
                .append("\";")
                .append("\",\")")
                .toString();
    }
}
