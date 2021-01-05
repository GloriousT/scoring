package com.myproject.client;

import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
@ToString
public class MacroTrendsClient {
    private final String ticker;
    private String companyPathName;

    public ValidatableResponse getEarningsHistory() {
        if (null == companyPathName) {
            fetchCompanyPathName();
        }
        return given()
                .log().uri().log().method()
                .get(String.format("https://www.macrotrends.net/stocks/charts/%s/%s/eps-earnings-per-share-diluted",
                        ticker,
                        companyPathName))
                .then().log().ifValidationFails();
    }

    private void fetchCompanyPathName() {
        var location = given().log().method().log().uri()
                .redirects().follow(false)
                .get(String.format("https://www.macrotrends.net/stocks/charts/%s", ticker))
                .then().log().ifValidationFails()
                .statusCode(301)
                .extract().header("Location")
                .split("/");
        companyPathName = location[location.length - 1];
    }
}
