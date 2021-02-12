package com.myproject.dto.yahoo.fundamental.v10.financial.data;

import com.myproject.dto.yahoo.fundamental.v10.FinancialReportItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialDataDto {
    private FinancialReportItem ebitda;
    private FinancialReportItem quickRatio;

    public BigDecimal getEBITDA() {
        return ebitda.getRawValue();
    }

    public BigDecimal getQuickRatio() {
        return quickRatio.getRawValue();
    }
}
