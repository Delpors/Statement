package com.example.statement.dto.respons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class ReportResponse
{
    private Long employeeId;
    private String fullName;
    private Map<String, MonthlyPayrollData> monthlyData;
    private BigDecimal totalSalary;
    private BigDecimal totalTax;
    private BigDecimal totalUnionFee;
    private Report report;

    @Getter
    public enum Report
    {
        EMPL_YEAR_PAY("Отчет по заработной плате"),
        TAXES("Отчет по налогам");

        private final String displayName;

        Report(String displayName) {
            this.displayName = displayName;
        }

    }
}
