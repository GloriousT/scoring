package com.myproject.client;

import com.myproject.dto.yahoo.fundamental.v10.incomestatement.quarterly.IncomeStatementHistoryQuarterlyDto;
import com.myproject.dto.yahoo.price.v8.PriceChartDto;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.mapper.ObjectMapperType.GSON;
import static java.time.ZoneOffset.UTC;

/**
 * modules = [
 *    'assetProfile',
 *    'summaryProfile',
 *    'summaryDetail',
 *    'esgScores',
 *    'price',
 *    'incomeStatementHistory',
 *    'incomeStatementHistoryQuarterly',
 *    'balanceSheetHistory',
 *    'balanceSheetHistoryQuarterly',
 *    'cashflowStatementHistory',
 *    'cashflowStatementHistoryQuarterly',
 *    'defaultKeyStatistics',
 *    'financialData',
 *    'calendarEvents',
 *    'secFilings',
 *    'recommendationTrend',
 *    'upgradeDowngradeHistory',
 *    'institutionOwnership',
 *    'fundOwnership',
 *    'majorDirectHolders',
 *    'majorHoldersBreakdown',
 *    'insiderTransactions',
 *    'insiderHolders',
 *    'netSharePurchaseActivity',
 *    'earnings',
 *    'earningsHistory',
 *    'earningsTrend',
 *    'industryTrend',
 *    'indexTrend',
 *    'sectorTrend' ]
 */
@AllArgsConstructor
@ToString
public class YFinanceClient {
    private final String ticker;

    public PriceChartDto get10YearsPriceHistory() {
        LocalDateTime now = LocalDateTime.now();
        var end = now.withDayOfMonth(1).minusMonths(1);
        return given()
                .basePath("/v8/finance/chart")
                .queryParam("symbol", ticker)
                .queryParam("period1", end.minusYears(10).toEpochSecond(UTC))
                .queryParam("period2", end.toEpochSecond(UTC))
                .queryParam("interval", "3mo")
                .get(ticker)
                .then().log().ifValidationFails()
                .statusCode(200)
                .extract().as(PriceChartDto.class, GSON);
    }

    public RequestSpecification getFundamentalData(String module) {
        return given()
                .basePath("/v10/finance/quoteSummary/" + ticker)
                .queryParam("modules", module);
    }

    public RequestSpecification given() {
        return RestAssured.given()
                .log().uri().log().method()
                .baseUri("https://query1.finance.yahoo.com");
    }

    public IncomeStatementHistoryQuarterlyDto getIncomeStatement() {
         return getFundamentalData("incomeStatementHistoryQuarterly")
                .get()
                .then()
                .log().all()
                .statusCode(200)
                .extract().jsonPath()
                .getObject("quoteSummary.result[0].incomeStatementHistoryQuarterly",
                        IncomeStatementHistoryQuarterlyDto.class);
    }
}
