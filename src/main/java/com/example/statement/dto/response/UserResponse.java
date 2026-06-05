package com.example.statement.dto.response;

import com.example.statement.util.UserRole;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String userName,
        String password,
        String email,
        Set<UserRole> role,
        LocalDateTime createdAt,
        LocalDateTime deletedAt,
        boolean isActive
) {
}
