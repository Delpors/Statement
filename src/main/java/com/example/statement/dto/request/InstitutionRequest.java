package com.example.statement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InstitutionRequest(
        @NotBlank()
        String institutionFullName,
        @NotBlank
        String institutionAbbrev,
        @NotBlank
        String director,
        @NotBlank
        String generalAccountant,
        String accountant
) {
}
