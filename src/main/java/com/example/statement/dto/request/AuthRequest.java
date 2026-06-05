package com.example.statement.dto.request;

import jakarta.validation.constraints.NotNull;

public record AuthRequest (
        @NotNull String userName,
        @NotNull String password
){
}
