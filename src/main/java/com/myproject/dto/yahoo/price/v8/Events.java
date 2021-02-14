package com.myproject.dto.yahoo.price.v8;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class Events {
    private Map<String, DividendEvent> dividends;

    @Expose(serialize = false, deserialize = false)
    private List<DividendEvent> last10YearsDivs;

    public List<DividendEvent> getOrderedDividends() {
        return dividends.entrySet()
                .stream()
                .sorted(Comparator.comparing(it -> it.getValue().getDate()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public boolean hasRegularDividendPayments10YearsHistory() {
        var last10YearsDividends = getLast10YearsDivs();
        var stablePayoutGrowthHistory = hasStablePayoutGrowthHistory();
        //assuming divs paid quarterly
        return last10YearsDividends.size() == 40 && stablePayoutGrowthHistory;
    }

    private boolean hasStablePayoutGrowthHistory() {
        var last10YearsDividends = getYearlyPayouts();
        var isStable = true;
        for (int i = 0; i < last10YearsDividends.size() - 1; i++) {
            if (last10YearsDividends.get(i).compareTo(last10YearsDividends.get(i + 1)) <= 0) {
                isStable = false;
                break;
            }
        }
        return isStable;
    }

    private List<BigDecimal> getYearlyPayouts() {
        var last10YearsDividends = getLast10YearsDivs();
        List<BigDecimal> yearSums = new ArrayList<>();
        for (int i = 0; i < last10YearsDividends.size(); i+=4) {
            BigDecimal currentSum = BigDecimal.ZERO.add(last10YearsDividends.get(i).getAmount())
                    .add(last10YearsDividends.get(i + 1).getAmount())
                    .add(last10YearsDividends.get(i + 2).getAmount())
                    .add(last10YearsDividends.get(i + 3).getAmount());
            yearSums.add(currentSum);
        }
        return yearSums;
    }

    public List<DividendEvent> getLast10YearsDivs() {
        if (null == last10YearsDivs) {
            var divs = getOrderedDividends();
            Collections.reverse(divs);
//            var lastDividend = LocalDateTime.ofEpochSecond(divs.get(0).getDate(), 0, UTC);
            //assuming divs paid quarterly
            last10YearsDivs = divs.stream()
                    .limit(40)
//                    .filter(it -> {
//                        var currentDivDate = LocalDateTime.ofEpochSecond(it.getDate(), 0, UTC);
//                        var duration = Duration.between(currentDivDate, lastDividend);
//                        return duration.compareTo(Duration.ofDays(3653)) < 0;
//                    })
                    .collect(Collectors.toList());
        }
        return last10YearsDivs;
    }
}
