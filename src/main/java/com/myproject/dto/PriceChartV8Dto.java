package com.myproject.dto;

import groovyjarjarantlr4.v4.misc.OrderedHashMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


@Data
@Slf4j
public class PriceChartV8Dto {
    private Chart chart;

    @Data
    public static class Chart {
        private List<Result> result;

        private Result getResult() {
            return result.get(0);
        }

        @Data
        public static class Result {
            private List<Long> timestamp;
            private Indicators indicators;

            @Data
            public static class Indicators {
                private List<AdjClose> adjclose;

                private AdjClose getAdjclose() {
                    return adjclose.get(0);
                }

                @Data
                public static class AdjClose {
                    private List<BigDecimal> adjclose;
                }
            }
        }
    }

    public BigInteger get10YearsPriceChange() {
        var prices = new ArrayList<>(getPrices().values());
        return getAverageFor40Elements(prices)
                .multiply(BigDecimal.valueOf(100))
                .toBigInteger();
    }

    private BigDecimal getAverageFor40Elements(ArrayList<BigDecimal> prices) {
        int size = prices.size();
        if (40 != size) {
            throw new RuntimeException("Size of elements is expected to be 40 but was: " + size);
        }
        log.info("Start elements:");
        var startElements = prices.stream()
                .limit(12)
                .peek(it -> log.info(it.toString()))
                .collect(toList());
        var endElements = prices.subList(size - 12, size);
        log.info("End elements:");
        endElements.forEach(it -> log.info(it.toString()));
        var avgStart = getAverage(startElements);
        var avgEnd = getAverage(endElements);
        return avgEnd.subtract(avgStart).divide(avgStart, MathContext.DECIMAL64);
    }

    private BigDecimal getAverage(List<BigDecimal> startElements) {
        return startElements
                .stream()
                .reduce(BigDecimal::add)
                .get().divide(BigDecimal.valueOf(12), MathContext.DECIMAL64);
    }

    public Map<LocalDateTime, BigDecimal> getPrices() {
        var result = this.chart.getResult();
        var timestamps = result.timestamp
                .stream()
                .limit(40)
                .collect(toList());
        var prices = result.indicators.getAdjclose().adjclose;
        Map<LocalDateTime, BigDecimal> pricesWithDate = new OrderedHashMap<>();
        timestamps
                .forEach(it -> {
                    int currentIndex = timestamps.indexOf(it);
                    pricesWithDate.put(
                            LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC),
                            prices.get(currentIndex));
                });
        pricesWithDate.forEach((key, value) -> log.info(key + "  " + value));
        return pricesWithDate;
    }
}
