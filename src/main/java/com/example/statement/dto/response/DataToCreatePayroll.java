package com.example.statement.dto.response;

import java.math.BigDecimal;

public record DataToCreatePayroll(
        Long employeeId,
        Long institutionId,
        String fullName,
        BigDecimal nonTaxable,
        String position,
        BigDecimal baseSalary
) {

}
