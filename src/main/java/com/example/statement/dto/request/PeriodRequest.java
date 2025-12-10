package com.example.statement.dto.request;

import java.time.LocalDate;

public record PeriodRequest(
        PeriodType periodType,
        Integer month,
        Integer year,
        LocalDate startDate,
        LocalDate endDate,
        String quarter
) {
    public enum PeriodType{
        CURRENT_MONTH,
        PREVIOUS_MONTH,
        SPECIFIC_MONTH,
        CUSTOM,
        QUARTER,
        YEAR
    }
}
