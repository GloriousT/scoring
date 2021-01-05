package com.myproject;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static com.myproject.service.ServiceProvider.financeService;


@Slf4j
public class App {


    @SneakyThrows
    public static void main(String[] args) {
        var finances = financeService("LMT");
//        finances.getPriceChange();
//        System.out.println(finances.getEarningsChange());
        System.out.println(finances.getPriceGrowthToEarningsGrowthRatio());

    }
}
