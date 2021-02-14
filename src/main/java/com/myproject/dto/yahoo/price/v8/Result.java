package com.myproject.dto.yahoo.price.v8;

import lombok.Data;

import java.util.List;

@Data
public class Result {
    private List<Long> timestamp;
    private Indicators indicators;
    private Events events;
}
