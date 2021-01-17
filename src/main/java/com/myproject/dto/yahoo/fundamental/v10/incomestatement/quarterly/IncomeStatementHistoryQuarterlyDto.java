package com.myproject.dto.yahoo.fundamental.v10.incomestatement.quarterly;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@Data
@Slf4j
public class IncomeStatementHistoryQuarterlyDto {
    private List<IncomeStatementHistoryItem> incomeStatementHistory;
    private int maxAge;

    //TTM = trailing twelve months
    public BigDecimal getEbitTTM() {
        return incomeStatementHistory.stream()
                .map(IncomeStatementHistoryItem::getRawEbitValue)
                .peek(it -> log.info(it.toString()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getLastQuarterEBIT() {
        return incomeStatementHistory.get(0).getRawEbitValue();
    }

    public BigDecimal getInterestExpenseTTM() {
        return incomeStatementHistory.stream()
                .map(IncomeStatementHistoryItem::getRawInterestExpense)
                .peek(it -> log.info("Interests expense :{}", it))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getLastQuarterInterestExpense() {
        return incomeStatementHistory.get(0).getRawInterestExpense();
    }

    public BigDecimal getInterestCoverage() {
        //should I really use TTM or just last quarter is better?
//        var ebitTTM = getEbitTTM();
//        var interestExpense = getInterestExpenseTTM();
//        log.info("EBIT TTM: {}", ebitTTM);
//        log.info("Interest Expense TTM: {}", interestExpense);
        var ebit = getLastQuarterEBIT();
        var interestExpense = getLastQuarterInterestExpense().multiply(BigDecimal.valueOf(-1));
        log.info("EBIT: {}", ebit);
        log.info("Interest Expense: {}", interestExpense);
        return ebit.divide(interestExpense, MathContext.DECIMAL64)
                .setScale(2, RoundingMode.CEILING);
    }
}
