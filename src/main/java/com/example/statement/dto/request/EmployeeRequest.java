package com.example.statement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record EmployeeRequest(
        @NotBlank(message = "Имя должна быть указана обязательно")
        String name,

        @NotBlank(message = "Фамилия должна быть указана обязательно.")
        String surName,

        String lastname,

        @NotBlank(message = "Должность должен быть указ обязательно")
        String position,

        @Positive(message = "Необлагаемая сумма не может быть отрицательным число")
        BigDecimal nonTaxable,

        @NotNull(message = "Оклад обязательно должен быть указан.")
        @Positive(message = "Оклад не может быть отрицательным числом.")
        BigDecimal salary,

        @NotBlank(message = "Расчетный счет должен быть указ обязательно")
        String bankAccount,

        @Email(message = "Почтовый адрес должен быть указ обязательно")
        String email
) {
}
