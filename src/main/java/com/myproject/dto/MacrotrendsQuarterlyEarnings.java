package com.myproject.dto;

import com.myproject.calculations.AverageCalculation;
import groovyjarjarantlr4.v4.misc.OrderedHashMap;
import io.restassured.path.xml.element.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor(access = PRIVATE)
public class MacrotrendsQuarterlyEarnings {
    private Map<LocalDate, BigDecimal> earnings;

    public static MacrotrendsQuarterlyEarnings from(Node strings) {
        Map<LocalDate, BigDecimal> earnings = new LinkedHashMap<>();
        var counter = new AtomicInteger();
        strings.forEach(it -> {
            int counterCurrent = counter.get();
            if (counterCurrent == 40) {
                return;
            }
            log.debug("handling element {}, {}", counterCurrent, it);
            var earningItem = it.split("\\$");
            earnings.put(LocalDate.parse(earningItem[0]), new BigDecimal(earningItem[1]));
            counter.getAndIncrement();
        });
        log.info("Retrieved earnings {}", earnings);
        return new MacrotrendsQuarterlyEarnings(earnings);
    }

    public BigInteger get10YearsEpsChange() {
        var entries = new ArrayList<>(earnings.values());
        //we need to reverse as the latest elements appear as the first elements and vice versa
        Collections.reverse(entries);
        return new AverageCalculation(entries).getChangeFor40Elements();
    }
}
