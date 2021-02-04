package com.myproject.service;

import com.myproject.client.MacroTrendsClient;
import com.myproject.client.YFinanceClient;


public class ServiceProvider {

    public static FinanceServiceFacade financeService(String ticker) {
        return new FinanceServiceFacade(
                ticker,
                new YFinanceService(new YFinanceClient(ticker)),
                new MacroTrendsFinanceService(new MacroTrendsClient(ticker)));
    }
}
