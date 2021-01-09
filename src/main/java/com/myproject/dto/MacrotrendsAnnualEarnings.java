package com.myproject.dto;

import com.myproject.calculations.AverageCalculation;
import io.restassured.path.xml.element.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor(access = PRIVATE)
public class MacrotrendsAnnualEarnings {
    private Map<Integer, BigDecimal> earnings;

    public static MacrotrendsAnnualEarnings from(Node annualEarnings) {
        Map<Integer, BigDecimal> earnings = new LinkedHashMap<>();
        annualEarnings.children().list().stream()
                .limit(11)
                .forEach(it -> {
                    log.debug("handling element {}", it);
                    var earningItem = it.toString().split("\\$");
                    earnings.put(Integer.valueOf(earningItem[0]), new BigDecimal(earningItem[1]));
                });
        log.info("Retrieved annual earnings {}", earnings);
        return new MacrotrendsAnnualEarnings(earnings);
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
}
