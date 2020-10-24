package com.myproject.dto;

import groovyjarjarantlr4.v4.misc.OrderedHashMap;
import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;


@Data
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

    public BigDecimal getPriceChange() {
        var prices = getPrices().values();
        return prices
                .stream()
                .reduce(BigDecimal::add)
                .get().divide(BigDecimal.valueOf(40), MathContext.DECIMAL64);
    }

    public Map<LocalDateTime, BigDecimal> getPrices() {
        var result = this.chart.getResult();
        var timestamps = result.timestamp;
        timestamps.remove(timestamps.size() - 1);
        var prices = result.indicators.getAdjclose().adjclose;
        Map<LocalDateTime, BigDecimal> pricesWithDate = new OrderedHashMap<>();
        timestamps
                .forEach(it -> {
                    int currentIndex = timestamps.indexOf(it);
                    pricesWithDate.put(
                            LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC),
                            prices.get(currentIndex));
                });
        return pricesWithDate;
    }
}
