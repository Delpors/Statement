package com.example.statement.dto.request;

import com.example.statement.util.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Set;

public record UserRequest(
        @NotBlank String userName,
        @NotBlank String password,
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank Set<UserRole> role,
        @NotBlank LocalDateTime createdAt,
        LocalDateTime deletedAt,
        boolean isActive
) {
}
