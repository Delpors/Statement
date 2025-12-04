package com.example.statement.payroll;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public record PayrollDTO (

        Long payrollId,
        Long payrollItemsId,
        Long institutionId,
        BigDecimal totalIncome,
        BigDecimal totalUnionFee,
        BigDecimal totalIncomeTax,
        BigDecimal totalAdvance,
        BigDecimal totalIssued,
        LocalDate payrollDate,
        LocalDateTime createdAt,
        String status
){
}
