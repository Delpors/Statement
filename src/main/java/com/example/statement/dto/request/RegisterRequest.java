package com.example.statement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String userName,
        @NotBlank String confirmPassword,
        @NotBlank String password,
        @NotBlank String fullName,
        @NotBlank @Email String email

) {
}
