package com.example.statement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotNull String userName,
        @NotNull String password,
        @NotNull String fullName,
        @NotNull @Email String email

) {
}
