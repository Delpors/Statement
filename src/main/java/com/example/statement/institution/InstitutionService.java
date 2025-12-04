package com.example.statement.institution;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InstitutionService {

    private final InstitutionRepository instRepository;

    InstitutionService (InstitutionRepository instRepository){
        this.instRepository = instRepository;
    }

    public void createOrUpdateInstitution(InstitutionDTO institutionDTO){

        if (institutionDTO.institution_Id() == null) {
            createInstitution(institutionDTO);
        } else {

            boolean isExist = instRepository.existsById(institutionDTO.institution_Id());
            if (isExist) {
                updateInstitution(institutionDTO);
            }else {
                throw new NoSuchElementException("Организация с таким id не найдено");
            }
        }
    }

    private void createInstitution(InstitutionDTO institutionDTO) {
        instRepository.save(convertSingleDTOtoEntity(institutionDTO));
    }

    private void updateInstitution(InstitutionDTO institutionDTO) {

        InstitutionEntity inst = convertSingleDTOtoEntity(institutionDTO);

        InstitutionEntity existInst = instRepository
                .findById(inst.getInstitutionId())
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
                .orElseThrow(()-> new RuntimeException
                        ("Организация с id" + id + "не найдена"));

        return institutionEntity;
    }
    public InstitutionDTO getInstitutionDTOById(Long id){

        InstitutionEntity institutionEntity = instRepository.findById(id)
                .orElseThrow(()-> new RuntimeException
                        ("Организация с id" + id + "не найдена"));

        return convertSingleEntityToDTO(institutionEntity);
    }

    public List<InstitutionDTO> getAllInstitutions(){

        List<InstitutionEntity> existInstitutions = instRepository.findAll();
        return convertEntityToDTO(existInstitutions);
    }

    public List<InstitutionDTO> convertEntityToDTO(List<InstitutionEntity> instEntity){
        return instEntity
                .stream()
                .map(this::convertSingleEntityToDTO)
                .toList();
    }

    public InstitutionDTO convertSingleEntityToDTO(InstitutionEntity instEntity){
        return new InstitutionDTO(
                instEntity.getInstitutionId(),
                instEntity.getInstitutionFullName(),
                instEntity.getInstitutionAbbrev(),
                instEntity.getDirector(),
                instEntity.getGeneralAccountant(),
                instEntity.getAccountant()
        );
    }

    public InstitutionEntity convertSingleDTOtoEntity(InstitutionDTO instDTO){
        return new InstitutionEntity(
                instDTO.institution_Id(),
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
