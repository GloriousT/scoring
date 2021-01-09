package com.myproject.dto.macrotrends;

import com.myproject.calculations.AverageCalculation;
import io.restassured.path.xml.element.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@AllArgsConstructor(access = PRIVATE)
public class QuarterlyEarnings {
    private Map<LocalDate, BigDecimal> earnings;

    public static QuarterlyEarnings from(Node strings) {
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
        log.info("Retrieved quarterly earnings {}", earnings);
        return new QuarterlyEarnings(earnings);
    }

    public BigDecimal get10YearsEpsChange() {
        var entries = new ArrayList<>(earnings.values());
        //we need to reverse as the latest elements appear as the first elements and vice versa
        Collections.reverse(entries);
        return new AverageCalculation(entries).getChangeFor40Elements();
    }
}
