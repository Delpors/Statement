package com.example.statement.dto.request;

import java.math.BigDecimal;

public record EmployeeRequest(
        String name,
        String surName,
        String lastname,
        String position,
        BigDecimal nonTaxable,
        BigDecimal salary,
        String bankAccount,
        String email
) {
}
