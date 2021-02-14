package com.myproject.dto.yahoo.price.v8;

import lombok.Data;

import java.util.List;

@Data
public class Chart {
    private List<Result> result;

    public Result getResult() {
        return result.get(0);
    }
}