package com.myproject;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static com.myproject.service.ServiceProvider.financeService;


@Slf4j
public class App {


    @SneakyThrows
    public static void main(String[] args) {
        var finances = financeService("BGS");
//        var priceChange = finances.getPriceChange();
//        var earningsChange = finances.getEarningsChange();
//        var priceGrowthToEarningsGrowthRatio = finances.getPriceGrowthToEarningsGrowthRatio();
//        var trailingPe = finances.getTrailingPe();
//        var numberSignificantYoYEpsFalls= finances.getNumberSignificantYoYEpsFalls();
//        System.out.println("results==================================================");
//        System.out.println(priceChange);
//        System.out.println(earningsChange);
//        System.out.println(priceGrowthToEarningsGrowthRatio);
//        System.out.println(trailingPe);
//        System.out.println(numberSignificantYoYEpsFalls);
        System.out.println(finances.getInterestCoverage());

    }
}
