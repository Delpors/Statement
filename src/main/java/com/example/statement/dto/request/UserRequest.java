package com.example.statement.dto.request;

import com.example.statement.util.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UserRequest(
        @NotBlank String userName,
        @NotBlank String password,
        @NotBlank @Email String email,
        @NotBlank UserRole role,
        @NotBlank LocalDateTime createdAt,
        LocalDateTime deletedAt,
        boolean isActive
) {
}
