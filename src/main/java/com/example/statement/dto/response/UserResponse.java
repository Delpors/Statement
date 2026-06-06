package com.example.statement.dto.response;

import com.example.statement.util.UserRole;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String userName,
        String email,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime deletedAt,
        boolean isActive
) {
}
