package com.example.statement.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PayrollItemsRequest(
        String periodInfo,
        String fullName,
        BigDecimal nonTaxable,
        String position,
        BigDecimal baseSalary,
        BigDecimal bonus,
        BigDecimal fss ,
        BigDecimal replace ,
        BigDecimal otherIncome ,
        BigDecimal totalEmployeeIncome ,
        BigDecimal absent ,
        BigDecimal unionFee ,
        BigDecimal incomeTax ,
        BigDecimal advance ,
        BigDecimal totalEmployeeDeduction ,
        BigDecimal totalIssued,
        LocalDate paymentDate,
        LocalDateTime createdAt
) {
}
