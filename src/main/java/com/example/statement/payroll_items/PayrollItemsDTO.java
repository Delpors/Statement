package com.example.statement.payroll_items;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PayrollItemsDTO(
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
         LocalDate paymentDate,
         LocalDateTime createdAt
 ) {
}
