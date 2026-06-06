package com.example.statement.service;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.dto.response.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.UserEntity;
import com.example.statement.repository.InstitutionRepository;
import com.example.statement.service.converter.InstitutionConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InstitutionService implements IInstitutionService{

    private final InstitutionRepository instRepository;
    private final InstitutionConverter institutionConverter;

    public void createInstitution(InstitutionRequest request, UserEntity user) {
        instRepository.save(institutionConverter.toEntity(null,request, user));
    }

    @Transactional
    public void updateInstitution(Long id, InstitutionRequest request, UserEntity user) {

        InstitutionEntity inst = new InstitutionConverter().toEntity(id,request, user);

        InstitutionEntity existInst = instRepository
                .findById(id)
                .orElseThrow(()-> new NoSuchElementException
                        ("Организация с id" + inst.getId() + "не найдена"));

        existInst.setInstitutionFullName(inst.getInstitutionFullName());
        existInst.setInstitutionAbbrev(inst.getInstitutionAbbrev());
        existInst.setDirector(inst.getDirector());
        existInst.setGeneralAccountant(inst.getGeneralAccountant());
        existInst.setAccountant(inst.getAccountant());
    }

    @Transactional
    public void deleteInstitution(Long id){
        if (!instRepository.existsById(id)){
            throw new NoSuchElementException("Организация с id" + id + "не найдена");
        }

        instRepository.deleteById(id);
    }

    public InstitutionEntity getInstitutionEntityById(Long id){

        return instRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException
                        ("Организация с id" + id + "не найдена"));
    }

    public InstitutionResponse getInstitutionDTOById(Long id){

        InstitutionEntity institutionEntity = instRepository.findById(id)
                .orElseThrow(()-> new RuntimeException
                        ("Организация с id" + id + "не найдена"));

        return new InstitutionConverter().toSingleResponse(institutionEntity);
    }

    public List<InstitutionResponse> getAllInstitutions(){

        List<InstitutionEntity> existInstitutions = instRepository.findAll();
        return institutionConverter.toResponse(existInstitutions);
    }

}
