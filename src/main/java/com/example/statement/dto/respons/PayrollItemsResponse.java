package com.example.statement.dto.respons;

import com.example.statement.entity.PayrollEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PayrollItemsResponse(
         Long payrollItemId,
         Long employeeId,
         Long institutionId,
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
         String period,
         Integer month,
         Integer year,
         LocalDate paymentDate,
         LocalDateTime createdAt
 ) {}
