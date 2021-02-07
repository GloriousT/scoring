package com.myproject.dto.yahoo.fundamental.v10.incomestatement.quarterly;

import com.myproject.dto.yahoo.fundamental.v10.FinancialReportItem;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

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
        return ebit.getRawValue();
    }

    public BigDecimal getRawInterestExpense() {
        return interestExpense.getRawValue();
    }
}
