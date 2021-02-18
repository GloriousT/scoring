package com.myproject;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.myproject.service.ServiceProvider.financeService;


@Slf4j
public class App {

    public static void main(String[] args) {
        Stream.of(
        "QDEL",
                "CRM"
        )
                .map(it -> financeService(it).getFullEvaluation().toGoogleSheetSplit())
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }
}
