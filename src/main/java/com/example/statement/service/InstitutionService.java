package com.example.statement.service;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.dto.response.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.UserEntity;
import com.example.statement.repository.InstitutionRepository;
import com.example.statement.service.converter.InstitutionConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstitutionService implements IInstitutionService{

    private final InstitutionRepository instRepository;
    private final InstitutionConverter institutionConverter;

    @Transactional
    public void createInstitution(InstitutionRequest request, UserEntity user) {

        log.info("Попытка добавить учреждение {} в базу.",request.institutionAbbrev());
        instRepository.save(institutionConverter.toEntity(null,request, user));
        log.debug("Учреждение {}, успешно добавлено в базу", request.institutionAbbrev());
    }

    @Transactional
    public void updateInstitution(Long instId, InstitutionRequest request, UserEntity user) {

        log.info("Попытка обновить данные учреждения c id: {}",instId);
        InstitutionEntity inst = new InstitutionConverter().toEntity(instId,request, user);

        InstitutionEntity existInst = instRepository
                .findById(instId)
                .orElseThrow(()-> new NoSuchElementException
                        ("Организация с id" + inst.getId() + "не найдена"));

        existInst.setInstitutionFullName(inst.getInstitutionFullName());
        existInst.setInstitutionAbbrev(inst.getInstitutionAbbrev());
        existInst.setDirector(inst.getDirector());
        existInst.setGeneralAccountant(inst.getGeneralAccountant());
        existInst.setAccountant(inst.getAccountant());

        log.debug("Данные учреждения с id {}, успешно обновлены.",instId);
    }

    @Transactional
    public void deleteInstitution(Long id){

        log.info("Попытка удалить учреждение с id {} из базы.",id);

        if (!instRepository.existsById(id)){
            throw new NoSuchElementException("Организация с id" + id + "не найдена");
        }
        instRepository.deleteById(id);

        log.debug("Учреждение с id {}, успешно удалено из базы.",id);
    }

    public InstitutionEntity getInstitutionEntityById(Long instId){

        return instRepository.findById(instId)
                .orElseThrow(()-> new NoSuchElementException
                        ("Организация с id" + instId + "не найдена"));
    }

    public InstitutionResponse getInstitutionDTOById(Long id){

        InstitutionEntity institutionEntity = instRepository.findById(id)
                .orElseThrow(()-> new RuntimeException
                        ("Организация с id" + id + "не найдена"));

        return new InstitutionConverter().toSingleResponse(institutionEntity);
    }

    public List<InstitutionResponse> getAllInstitutions(UserEntity user){

        List<InstitutionEntity> existInstitutions = instRepository.findAllByUser(user);
        return institutionConverter.toResponse(existInstitutions);
    }

}
