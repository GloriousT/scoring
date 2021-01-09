package com.myproject.client;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static io.restassured.RestAssured.given;

@Slf4j
public class MacroTrendsClient {
    public static final String BASE_URI = "https://www.macrotrends.net/stocks/charts";
    public static final String EPS_DILUTED_PATH = "eps-earnings-per-share-diluted";
    public static final String PE_RATIO_PATH = "pe-ratio";

    private final String ticker;
    private final String companyPath;

    private List<Node> earnings;

    public MacroTrendsClient(String ticker) {
        this.ticker = ticker;
        companyPath = resolveCompanyPath();
    }

    public Node getQuarterlyEarningsHistory() {
        var quarterlyEarningsNode = getEarningsHistory().get(1).children();
        if (!quarterlyEarningsNode.get(0).toString().contains("Quarterly EPS")) {
            throw new RuntimeException("Can't read quarterly EPS for " + ticker);
        }
        return quarterlyEarningsNode.get(1);
    }

    public Node getAnnualEarningsHistory() {
        var annualEarnings = getEarningsHistory().get(0).children();
        if (!annualEarnings.get(0).toString().contains("Annual EPS")) {
            throw new RuntimeException("Can't read annual EPS for " + ticker);
        }
        return annualEarnings.get(1);
    }


    public List<Node> getEarningsHistory() {
        if (null == earnings) {
            log.info("Earnings for {} are not retrieved yet, fetching...", ticker);
            earnings = given()
                    .get(String.format("/%s/%s",
                            companyPath, EPS_DILUTED_PATH))
                    .then().log().ifValidationFails()
                    .statusCode(200)
                    .extract().htmlPath().getList("**.findAll { it.@class == 'historical_data_table table' }", Node.class);
        }
        return earnings;
    }

    public XmlPath getPriceToEarningsHistory() {
        return given()
                .get(String.format("/%s/%s",
                        companyPath, PE_RATIO_PATH))
                .then().log().ifValidationFails()
                .statusCode(200)
                .extract().htmlPath();
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
        return String.format("%s/%s", ticker, companyFullName);
    }

    private RequestSpecification given() {
        return RestAssured.given()
                .log().method().log().uri()
                .baseUri(BASE_URI);
    }
}
