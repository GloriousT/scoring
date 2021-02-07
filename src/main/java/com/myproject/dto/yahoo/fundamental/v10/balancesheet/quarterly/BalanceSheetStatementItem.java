package com.myproject.dto.yahoo.fundamental.v10.balancesheet.quarterly;

import com.myproject.dto.yahoo.fundamental.v10.FinancialReportItem;
import com.myproject.dto.yahoo.fundamental.v10.incomestatement.quarterly.PeriodEndDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BalanceSheetStatementItem {
    private int maxAge;
    private PeriodEndDate endDate;
    private FinancialReportItem totalLiab;
    private FinancialReportItem totalCurrentAssets;

    public BigDecimal getTotalLiabilities() {
        return totalLiab.getRawValue();
    }

    public BigDecimal getCurrentAssets() {
        return totalCurrentAssets.getRawValue();
    }
}
