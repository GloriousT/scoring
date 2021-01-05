package com.myproject.client;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

@AllArgsConstructor
@ToString
public class YFinanceClient {
    private final String ticker;

    public ValidatableResponse get10YearsPriceHistory() {
        LocalDateTime now = LocalDateTime.now();
        var end = now.withDayOfMonth(1).minusMonths(1);
        return given()
                .basePath("/v8/finance/chart")
                .queryParam("symbol", ticker)
                .queryParam("period1", end.minusYears(10).toEpochSecond(UTC))
                .queryParam("period2", end.toEpochSecond(UTC))
                .queryParam("interval", "3mo")
                .get(ticker)
                .then().log().ifValidationFails();
    }

    public RequestSpecification getSummary(String module) {
        return given()
                .basePath("/v10/finance/quoteSummary/" + ticker)
                .queryParam("modules", module);
    }

    public RequestSpecification given() {
        return RestAssured.given()
                .log().uri().log().method()
                .baseUri("https://query1.finance.yahoo.com");
    }
}
