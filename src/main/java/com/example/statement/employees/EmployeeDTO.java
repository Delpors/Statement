package com.example.statement.employees;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeDTO(
         Long employee_id,
         Long institutionId,
         String name,
         String surName,
         String lastname,
         String position,
         BigDecimal nonTaxable,
         BigDecimal salary,
         String bankAccount,
         String email,
         Boolean active,
         LocalDate deletedAt
) {
}
