package com.myproject.dto.macrotrends;

import io.restassured.path.xml.element.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor(access = PRIVATE)
public class AnnualEarnings {
    private Map<Integer, BigDecimal> earnings;

    public static AnnualEarnings from(Node annualEarnings) {
        Map<Integer, BigDecimal> earnings = new LinkedHashMap<>();
        annualEarnings.children().list().stream()
                .limit(11)
                .forEach(it -> {
                    log.debug("handling element {}", it);
                    var earningItem = it.toString().split("\\$");
                    earnings.put(Integer.valueOf(earningItem[0]), new BigDecimal(earningItem[1]));
                });
        log.info("Retrieved annual earnings {}", earnings);
        return new AnnualEarnings(earnings);
    }

    public int getEarningsFallsCountGreaterThan(int percents) {
        var counter = 0;
        var values = new ArrayList<>(earnings.values());
        for (int i = 0; i < 10; i++) {
            if (values.get(i).divide(values.get(i + 1), MathContext.DECIMAL64)
                    .setScale(2, RoundingMode.CEILING).compareTo(BigDecimal.valueOf(0.95)) == -1) {
                counter++;
            }
        }
        return counter;
    }

    public Boolean isNegative10YearTrailingNetIncomePresent() {
        return earnings.values()
                .stream()
                .anyMatch(it -> it.compareTo(BigDecimal.ZERO) < 0);
    }
}
