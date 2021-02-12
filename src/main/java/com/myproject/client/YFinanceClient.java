package com.myproject.client;

import com.myproject.dto.yahoo.fundamental.v10.balancesheet.quarterly.BalanceSheetHistoryQuarterlyDto;
import com.myproject.dto.yahoo.fundamental.v10.financial.data.FinancialDataDto;
import com.myproject.dto.yahoo.fundamental.v10.incomestatement.quarterly.IncomeStatementHistoryQuarterlyDto;
import com.myproject.dto.yahoo.fundamental.v10.keystatistics.KeyStatisticsDto;
import com.myproject.dto.yahoo.price.v8.PriceChartDto;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

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
@RequiredArgsConstructor
@ToString
public class YFinanceClient {
    private final String ticker;

    private JsonPath fundamentalData;

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

    public Object getDividendHistory() {
        var now = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .toEpochSecond(UTC);
        var end = 466819200;
        return given()
                .basePath("/v8/finance/chart")
                .queryParam("symbol", ticker)
                .queryParam("period1", end)
                .queryParam("period2", now)
                .queryParam("interval", "1mo")
                .queryParam("events", "div")
                .get(ticker)
                .then().log().all()
                .statusCode(200)
                .extract().as(Object.class, GSON);
    }

    private JsonPath getFundamentalData() {
        if (fundamentalData == null) {
            fundamentalData = given()
                    .basePath("/v10/finance/quoteSummary/" + ticker)
                    .queryParam("modules",
                            String.join(",",
                                    "incomeStatementHistoryQuarterly",
                                    "defaultKeyStatistics",
                                    "financialData",
                                    "balanceSheetHistoryQuarterly"))
                    .get()
                    .then().log().all()
                    .statusCode(200)
                    .extract().jsonPath();
        }
        return fundamentalData;
    }

    public RequestSpecification given() {
        return RestAssured.given()
                .log().uri().log().method()
                .baseUri("https://query1.finance.yahoo.com");
    }

    public IncomeStatementHistoryQuarterlyDto getIncomeStatement() {
        return getFundamentalData()
                .getObject(module("incomeStatementHistoryQuarterly"), IncomeStatementHistoryQuarterlyDto.class);
    }

    public BalanceSheetHistoryQuarterlyDto getBalanceSheet() {
        return getFundamentalData()
                .getObject(module("balanceSheetHistoryQuarterly"), BalanceSheetHistoryQuarterlyDto.class);
    }


    public KeyStatisticsDto getKeyStatistics() {
        return getFundamentalData()
                .getObject(module("defaultKeyStatistics"), KeyStatisticsDto.class);
    }

    public FinancialDataDto getFinancialData() {
        return getFundamentalData()
                .getObject(module("financialData"), FinancialDataDto.class);
    }

    private String module(String moduleName) {
        return "quoteSummary.result[0]." + moduleName;
    }
}
