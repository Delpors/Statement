package com.example.statement.service;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.dto.respons.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.repository.InstitutionRepository;
import com.example.statement.service.converter.InstitutionConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InstitutionService {

    private final InstitutionRepository instRepository;
    private final InstitutionConverter institutionConverter;

    InstitutionService (InstitutionRepository instRepository,
                        InstitutionConverter institutionConverter)
    {
        this.instRepository = instRepository;
        this.institutionConverter = institutionConverter;
    }

    public void createInstitution(InstitutionRequest request) {
        instRepository.save(new InstitutionConverter().toEntity(null,request));
    }

    public void updateInstitution(Long id, InstitutionRequest request) {

        InstitutionEntity inst = new InstitutionConverter().toEntity(id,request);

        InstitutionEntity existInst = instRepository
                .findById(id)
                .orElseThrow(()-> new NoSuchElementException
                        ("Организация с id" + inst.getInstitutionId() + "не найдена"));

        existInst.setInstitutionFullName(inst.getInstitutionFullName());
        existInst.setInstitutionAbbrev(inst.getInstitutionAbbrev());
        existInst.setDirector(inst.getDirector());
        existInst.setGeneralAccountant(inst.getGeneralAccountant());
        existInst.setAccountant(inst.getAccountant());

        instRepository.save(existInst);
    }
    
    public void deleteInstitution(Long id){
        if (!instRepository.existsById(id)){
            throw new NoSuchElementException("Организация с id" + id + "не найдена");
        }

        instRepository.deleteById(id);
    }

    public InstitutionEntity getInstitutionEntityById(Long id){

        InstitutionEntity institutionEntity = instRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException
                        ("Организация с id" + id + "не найдена"));

        return institutionEntity;
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
