package com.myproject.dto.yahoo.fundamental.v10;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialReportItem {
    @SerializedName("raw")
    private float rawValue;
    @SerializedName("fmt")
    private String formattedValue;
    @SerializedName("longFmt")
    private String longFormattedValue;

    public BigDecimal getRawValue() {
        return new BigDecimal(BigDecimal.valueOf(rawValue).toPlainString());
    }
}
