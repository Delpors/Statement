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
public class PayrollSummaryResponse
{
    private Long employeeId;
    private String fullName;
    private Map<String, MonthlyPayrollData> monthlyData;
    private BigDecimal totalSalary;
    private BigDecimal totalTax;
    private BigDecimal totalUnionFee;
}
