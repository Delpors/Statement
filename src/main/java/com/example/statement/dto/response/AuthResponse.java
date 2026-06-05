package com.example.statement.dto.response;

public record AuthResponse(
        String token,
        String username,
        String fullName,
        String role
) {
}
