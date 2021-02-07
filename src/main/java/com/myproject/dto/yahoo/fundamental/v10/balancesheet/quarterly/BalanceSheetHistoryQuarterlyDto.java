package com.myproject.dto.yahoo.fundamental.v10.balancesheet.quarterly;

import java.math.BigDecimal;
import java.util.List;

public class BalanceSheetHistoryQuarterlyDto {
    private List<BalanceSheetStatementItem> balanceSheetStatements;
    private int maxAge;


    public BigDecimal getLatestTotalLiabilities() {
        return balanceSheetStatements.get(0).getTotalLiabilities();
    }

    public BigDecimal getLatestCurrentAssets() {
        return balanceSheetStatements.get(0).getCurrentAssets();
    }
}
