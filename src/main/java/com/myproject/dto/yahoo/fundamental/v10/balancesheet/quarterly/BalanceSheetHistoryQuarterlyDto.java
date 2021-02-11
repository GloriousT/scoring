package com.myproject.dto.yahoo.fundamental.v10.balancesheet.quarterly;

import java.math.BigDecimal;
import java.util.List;

public class BalanceSheetHistoryQuarterlyDto {
    private List<BalanceSheetStatementItem> balanceSheetStatements;
    private int maxAge;


    public BigDecimal getLatestTotalLiabilities() {
        return latestBalance().getTotalLiabilities();
    }

    public BigDecimal getLatestCurrentAssets() {
        return latestBalance().getCurrentAssets();
    }

    public BigDecimal getLongTermDebt() {
        return latestBalance().getLongTermDebt();
    }

    public BigDecimal getTotalAssets() {
        return latestBalance().getTotalAssets();
    }

    private BalanceSheetStatementItem latestBalance() {
        return balanceSheetStatements.get(0);
    }
}
