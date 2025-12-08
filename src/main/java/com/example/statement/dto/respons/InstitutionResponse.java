package com.example.statement.dto.respons;


public record InstitutionResponse(
        Long institution_Id,
        String institutionFullName,
        String institutionAbbrev,
        String director,
        String generalAccountant,
        String accountant
) {

}
