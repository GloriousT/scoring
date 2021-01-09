package com.myproject.calculations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@AllArgsConstructor
public class AverageCalculation {

    private final List<BigDecimal> elements;

    public BigDecimal getGrahamWeightedAverageFor40Elements() {
        int size = elements.size();
        if (40 != size) {
            throw new RuntimeException("Size of elements is expected to be 40 but was: " + size);
        }
        log.info("Start elements:");
        var startElements = elements.stream()
                .limit(12)
                .peek(it -> log.info(it.toString()))
                .collect(toList());
        var endElements = elements.subList(size - 12, size);
        log.info("End elements:");
        endElements.forEach(it -> log.info(it.toString()));
        var avgStart = getAverage(startElements);
        log.info("Average start: {}", avgStart);
        var avgEnd = getAverage(endElements);
        log.info("Average end: {}", avgEnd);
        return avgEnd.subtract(avgStart).divide(avgStart, MathContext.DECIMAL64);
    }

    public BigDecimal getChangeFor40Elements() {
        return getGrahamWeightedAverageFor40Elements()
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.CEILING);
    }

    public BigDecimal getAverage() {
        return getAverage(elements);
    }

    private BigDecimal getAverage(List<BigDecimal> elements) {
        return elements
                .stream()
                .reduce(BigDecimal::add)
                .get()
                .divide(BigDecimal.valueOf(elements.size()), MathContext.DECIMAL64);
    }
}
