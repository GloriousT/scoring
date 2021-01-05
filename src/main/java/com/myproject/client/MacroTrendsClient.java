package com.myproject.client;

import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import lombok.ToString;

import static io.restassured.RestAssured.given;

@AllArgsConstructor
@ToString
public class MacroTrendsClient {
    private final String ticker;

    public ValidatableResponse getEarningsHistory() {
        return given()
                .log().uri().log().method()
                .get("https://www.macrotrends.net/stocks/charts/LMT/lockheed-martin/eps-earnings-per-share-diluted")
                .then().log().ifValidationFails();
    }
}
