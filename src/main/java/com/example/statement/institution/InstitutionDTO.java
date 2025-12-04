package com.example.statement.institution;


public record InstitutionDTO(
        Long institution_Id,
        String institutionFullName,
        String institutionAbbrev,
        String director,
        String generalAccountant,
        String accountant
) {

}
