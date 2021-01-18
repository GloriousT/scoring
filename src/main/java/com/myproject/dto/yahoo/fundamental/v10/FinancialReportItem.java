package com.myproject.dto.yahoo.fundamental.v10;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class FinancialReportItem {
    @SerializedName("raw")
    private float rawValue;
    @SerializedName("fmt")
    private String formattedValue;
    @SerializedName("longFmt")
    private String longFormattedValue;
}
