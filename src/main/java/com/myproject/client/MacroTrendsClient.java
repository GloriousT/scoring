package com.myproject.client;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.ToString;

import static io.restassured.RestAssured.given;

@ToString
public class MacroTrendsClient {
    public static final String BASE_URI = "https://www.macrotrends.net/stocks/charts";
    public static final String EPS_DILUTED_PATH = "eps-earnings-per-share-diluted";
    public static final String PE_RATIO_PATH = "pe-ratio";

    private final String ticker;
    private final String companyPath;

    public MacroTrendsClient(String ticker) {
        this.ticker = ticker;
        companyPath = resolveCompanyPath();
    }

    public XmlPath getEarningsHistory() {
        return given()
                .get(String.format("/%s/%s",
                        companyPath, EPS_DILUTED_PATH))
                .then().log().ifValidationFails()
                .statusCode(200)
                .extract().htmlPath();
    }

    public ValidatableResponse getPriceToEarningsHistory() {
        return given()
                .get(String.format("/%s/%s",
                        companyPath, PE_RATIO_PATH))
                .then().log().ifValidationFails();
    }

    private String resolveCompanyPath() {
        var location = given()
                .redirects().follow(false)
                .get(String.format(ticker))
                .then().log().ifValidationFails()
                .statusCode(301)
                .extract().header("Location")
                .split("/");
        var companyFullName = location[location.length - 1];
        return String.format("/%s/%s", ticker, companyFullName);
    }

    private RequestSpecification given() {
        return RestAssured.given()
                .log().method().log().uri()
                .baseUri(BASE_URI);
    }
}
