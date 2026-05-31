package com.example.statement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(
         Long employee_id,
         Long institutionId,
         String surName,
         String name,
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
