package com.myproject.dto.yahoo.price.v8;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DividendEvent {
    private BigDecimal amount;
    private Long date;

}
