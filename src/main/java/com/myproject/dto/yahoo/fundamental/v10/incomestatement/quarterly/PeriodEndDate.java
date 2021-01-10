package com.myproject.dto.yahoo.fundamental.v10.incomestatement.quarterly;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PeriodEndDate {
    private long raw;
    private String fmt;
}
