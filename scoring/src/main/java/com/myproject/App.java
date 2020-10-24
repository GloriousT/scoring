package com.myproject;

import com.myproject.dto.PriceChartV8Dto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.time.LocalDateTime;

import static io.restassured.mapper.ObjectMapperType.GSON;
import static java.time.ZoneOffset.UTC;


public class App {
    public static void main(String[] args) {
//        getSummary("NOC", "price");
        var price = getPriceHistory("NOC")
                .extract().as(PriceChartV8Dto.class, GSON);
        price.getPrices().forEach((key, value) -> System.out.println(key + "  " + value));
        System.out.println(price.getPriceChange());
    }

    private static ValidatableResponse getPriceHistory(String ticker) {
        var start = LocalDateTime.of(2010, 12, 1, 0, 0, 0).toEpochSecond(UTC);
        var end = LocalDateTime.now().toEpochSecond(UTC);
        return given()
                .basePath("/v8/finance/chart")
                .queryParam("symbol", ticker)
                .queryParam("period1", start)
                .queryParam("period2", end)
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
