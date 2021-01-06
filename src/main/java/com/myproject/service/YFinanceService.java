package com.myproject.service;

import com.myproject.client.YFinanceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

@Slf4j
@AllArgsConstructor
public class YFinanceService {

    private final YFinanceClient financeClient;

    public BigInteger getPriceChange() {
        var price = financeClient.get10YearsPriceHistory();
        var priceChange = price.get10YearsPriceChange();
        log.info("Price change is {}%:", priceChange);
        return priceChange;
    }
}
