package com.myproject.dto.yahoo.price.v8;

import com.myproject.calculations.AverageCalculation;
import groovyjarjarantlr4.v4.misc.OrderedHashMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Map;

import static java.util.stream.Collectors.toList;


@Data
@Slf4j
public class PriceChartDto {
    private Chart chart;

    public BigDecimal get10YearsPriceChange() {
        var prices = new ArrayList<>(getPrices().values());
        return new AverageCalculation(prices).getChangeFor40Elements();
    }

    public Map<LocalDateTime, BigDecimal> getPrices() {
        var result = chart.getResult();
        var timestamps = result.getTimestamp()
                .stream()
                .limit(40)
                .collect(toList());
        var prices = result.getIndicators().getAdjclose().getAdjclose();
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

    public Events getDividends() {
        return chart.getResult().getEvents();
    }
}
