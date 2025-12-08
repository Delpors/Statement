package com.example.statement.dto.request;

public record InstitutionRequest(
        String institutionFullName,
        String institutionAbbrev,
        String director,
        String generalAccountant,
        String accountant
) {
}
