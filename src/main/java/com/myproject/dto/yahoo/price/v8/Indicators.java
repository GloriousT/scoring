package com.myproject.dto.yahoo.price.v8;

import lombok.Data;

import java.util.List;

@Data
public class Indicators {
    private List<AdjClose> adjclose;

    public AdjClose getAdjclose() {
        return adjclose.get(0);
    }
}
