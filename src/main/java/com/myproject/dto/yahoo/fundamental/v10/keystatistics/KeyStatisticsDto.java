package com.myproject.dto.yahoo.fundamental.v10.keystatistics;

import com.myproject.dto.yahoo.fundamental.v10.FinancialReportItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class KeyStatisticsDto {
    private FinancialReportItem priceToBook;

    public BigDecimal getPB() {
        return new BigDecimal(priceToBook.getFormattedValue());
    }
}
