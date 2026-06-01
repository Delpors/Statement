package com.example.statement.service;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.dto.response.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;

import java.util.List;

public interface IInstitutionService {

    void createInstitution(InstitutionRequest request);
    void updateInstitution(Long id, InstitutionRequest request);
    void deleteInstitution(Long id);
    InstitutionEntity getInstitutionEntityById(Long id);
    InstitutionResponse getInstitutionDTOById(Long id);
    List<InstitutionResponse> getAllInstitutions();

}
