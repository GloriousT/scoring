package com.myproject.dto.yahoo.fundamental.v10.financial.data;

import com.myproject.dto.yahoo.fundamental.v10.FinancialReportItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialDataDto {
    private FinancialReportItem ebitda;

    public BigDecimal getEBITDA() {
        return BigDecimal.valueOf(ebitda.getRawValue());
    }
}
