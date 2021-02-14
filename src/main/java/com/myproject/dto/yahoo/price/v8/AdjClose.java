package com.myproject.dto.yahoo.price.v8;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AdjClose {
    private List<BigDecimal> adjclose;
}
