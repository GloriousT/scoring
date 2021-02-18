package com.myproject.dto.macrotrends;

import com.myproject.calculations.AverageCalculation;
import io.restassured.path.xml.element.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor(access = PRIVATE)
public class QuarterlyPriceRatios {
    private final Map<LocalDate, BigDecimal> peRatios;

    public static QuarterlyPriceRatios from(List<Node> priceItemRows) {
        Map<LocalDate, BigDecimal> peRatios = new LinkedHashMap<>();
        priceItemRows.forEach(it -> {
            var rowElements = it.children();
            var date = LocalDate.parse(rowElements.get(0).toString());
            var peRatio = new BigDecimal(rowElements.get(3).toString());
            peRatios.put(date, peRatio);
        });
        log.info("Retrieved PE Ratios {}", peRatios);
        var cleanedPeRatios = removeOutstandingPe(peRatios);
        return new QuarterlyPriceRatios(cleanedPeRatios);
    }

    private static Map<LocalDate, BigDecimal> removeOutstandingPe(Map<LocalDate, BigDecimal> peRatios) {
        var cleanedPeRatios = new LinkedHashMap<LocalDate, BigDecimal>();
        peRatios.forEach((key, value) -> {
            //for too high PE ratios we don't count them and consider artifacts unless there are too many of them
            if (value.compareTo(new BigDecimal(200)) > 0) {
                cleanedPeRatios.put(key, BigDecimal.ZERO);
            } else {
                cleanedPeRatios.put(key, value);
            }
        });
        return cleanedPeRatios;
    }

    public BigDecimal get10YearsTrailingPE() {
        var entries = new ArrayList<>(peRatios.values());
        return new AverageCalculation(entries)
                .getAverage()
                .setScale(2, RoundingMode.CEILING);
    }
}
