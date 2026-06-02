package com.example.statement.service.converter;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.dto.response.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InstitutionConverter {

    public InstitutionConverter(){

    }

    public List<InstitutionResponse> toResponse(List<InstitutionEntity> instEntity){
        return instEntity
                .stream()
                .map(this::toSingleResponse)
                .toList();
    }

    public InstitutionResponse toSingleResponse(InstitutionEntity instEntity){
        return new InstitutionResponse(
                instEntity.getId(),
                instEntity.getInstitutionFullName(),
                instEntity.getInstitutionAbbrev(),
                instEntity.getDirector(),
                instEntity.getGeneralAccountant(),
                instEntity.getAccountant()
        );
    }

    public InstitutionEntity toEntity(Long id, InstitutionRequest instDTO){
        return new InstitutionEntity(
                id,
                null,
                null,
                null,
                null,
                instDTO.institutionFullName(),
                instDTO.institutionAbbrev(),
                instDTO.director(),
                instDTO.generalAccountant(),
                instDTO.accountant()
        );
    }
}
