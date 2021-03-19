package com.myproject.dto.yahoo.fundamental.v10.keystatistics;

import com.myproject.dto.yahoo.fundamental.v10.FinancialReportItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class KeyStatisticsDto {
    private FinancialReportItem priceToBook;
    private FinancialReportItem lastDividendDate;

    public BigDecimal getPB() {
        return new BigDecimal(priceToBook.getFormattedValue());
    }

    public LocalDate getLastDividend() {
        return LocalDate.parse(lastDividendDate.getFormattedValue());
    }
}
