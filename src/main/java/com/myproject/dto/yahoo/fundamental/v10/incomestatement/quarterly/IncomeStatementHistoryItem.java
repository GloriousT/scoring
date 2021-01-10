package com.myproject.dto.yahoo.fundamental.v10.incomestatement.quarterly;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class IncomeStatementHistoryItem {
    private int maxAge;
    private PeriodEndDate endDate;
    private FinancialReportItem ebit;
    private FinancialReportItem interestExpense;

    public BigDecimal getRawEbitValue() {
        return BigDecimal.valueOf(ebit.getRawValue());
    }

    public BigDecimal getRawInterestExpense() {
        return BigDecimal.valueOf(interestExpense.getRawValue());
    }
}
