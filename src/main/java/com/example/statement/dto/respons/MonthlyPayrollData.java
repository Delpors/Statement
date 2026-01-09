package com.example.statement.dto.respons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MonthlyPayrollData
{
    private BigDecimal salary;
    private BigDecimal inkomTax;
    private BigDecimal unionFee;
}
