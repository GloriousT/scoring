package com.myproject.dto.yahoo.price.v8;

import com.myproject.calculations.AverageCalculation;
import groovyjarjarantlr4.v4.misc.OrderedHashMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


@Data
@Slf4j
public class PriceChartDto {
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

    public BigDecimal get10YearsPriceChange() {
        var prices = new ArrayList<>(getPrices().values());
        return new AverageCalculation(prices).getChangeFor40Elements();
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
