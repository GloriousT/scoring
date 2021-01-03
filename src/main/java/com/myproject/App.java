package com.myproject;

import com.myproject.dto.PriceChartV8Dto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static io.restassured.mapper.ObjectMapperType.GSON;
import static java.time.ZoneOffset.UTC;


@Slf4j
public class App {

    public static void main(String[] args) {
        var price = getPriceHistory("LMT")
                .extract().as(PriceChartV8Dto.class, GSON);
        price.getPrices()
                .forEach((key, value) -> log.info(key + "  " + value));
        log.info("Price change in {}%:", price.getPriceChange());
    }

    private static ValidatableResponse getPriceHistory(String ticker) {
        LocalDateTime now = LocalDateTime.now();
        var end = now.withDayOfMonth(1).minusMonths(1);
        return given()
                .basePath("/v8/finance/chart")
                .queryParam("symbol", ticker)
                .queryParam("period1", end.minusYears(10).toEpochSecond(UTC))
                .queryParam("period2", end.toEpochSecond(UTC))
                .queryParam("interval", "3mo")
                .get(ticker)
                .then().log().all();
    }

    private static ValidatableResponse getSummary(String ticker, String module) {
        return given()
                .basePath("/v10/finance/quoteSummary")
                .queryParam("modules", module)
                .get(ticker)
                .then().log().all();
    }

    private static RequestSpecification given() {
        return RestAssured.given()
                .log().all()
                .baseUri("https://query1.finance.yahoo.com");
    }
}
