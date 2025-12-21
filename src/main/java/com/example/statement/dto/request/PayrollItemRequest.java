package com.example.statement.dto.request;

import com.example.statement.entity.EmployeeEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PayrollItemRequest(
        Long employeeId,
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
        Integer month,
        Integer year,
        LocalDate paymentDate,
        LocalDateTime createdAt
) {
}
