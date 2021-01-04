package com.myproject.service;

import com.myproject.client.MacroTrendsClient;
import com.myproject.client.YFinanceClient;

public class ServiceProvider {

    public static FinanceService financeService(String ticker) {
        return new FinanceService(new YFinanceClient(ticker), new MacroTrendsClient(ticker));
    }
}
