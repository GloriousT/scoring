package com.myproject.client;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static java.time.ZoneOffset.UTC;

@AllArgsConstructor
@ToString
public class MacroTrendsClient {
    private final String ticker;

    public ValidatableResponse getEarningsHistory() {
        return given()
                .log().all()
                .get("https://www.macrotrends.net/stocks/charts/LMT/lockheed-martin/eps-earnings-per-share-diluted")
                .then().log().ifValidationFails();
    }
}
