package com.example.statement.service;

import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.respons.EmployeeResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    public InstitutionService institutionService;

    public EmployeeService(EmployeeRepository repository,
                           InstitutionService institutionService) {
        this.repository = repository;
        this.institutionService = institutionService;

    }

    public EmployeeEntity getEmployeeById(Long id) {

        return repository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Не найден сотрудник с id " + id));
    }

    public EmployeeResponse getEmployeeDTOById(Long id) {

        EmployeeEntity employee = repository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Не найден сотрудник с id " + id));
        return convertSingleEntityToDTO(employee);
    }

    public List<EmployeeResponse> getAllEmployees(Long instId) {
        List<EmployeeEntity> employeeEntities = repository.findActiveByInstitutionId(instId);

        return convertEntityToDTO(employeeEntities);
    }

    public List<EmployeeEntity> getAllEmployeeEntities(Long instId) {

        return repository.findActiveByInstitutionId(instId);
    }

    public void createEmployee(EmployeeRequest request, Long instId) {

        InstitutionEntity institutionEntity = institutionService.getInstitutionEntityById(instId);
        repository.save(convertSingleEmployee(request, institutionEntity));
    }

    public void updateEmployee(EmployeeRequest request, Long instId) {


        InstitutionEntity institutionEntity = institutionService.getInstitutionEntityById(instId);
        repository.save(convertSingleEmployee(request, institutionEntity));
    }

    public void deleteEmployee(Long id) {

        EmployeeEntity employeeToDelete = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Сотрудник не найден"));

        employeeToDelete.softDelete();
        repository.save(employeeToDelete);
    }

    public EmployeeEntity convertSingleEmployee(EmployeeRequest employeeDTO, InstitutionEntity institution){

        try {
            return new EmployeeEntity(
                    null,
                    institution,
                    null,
                    employeeDTO.name(),
                    employeeDTO.surName(),
                    employeeDTO.lastname(),
                    employeeDTO.position(),
                    employeeDTO.nonTaxable(),
                    employeeDTO.salary(),
                    employeeDTO.bankAccount(),
                    employeeDTO.email(),
                    true,
                    null
                    );
        } catch (Exception e) {
            System.err.println("Ошибка конвертации DTO в Entity" + e.getMessage());
            return null;
        }
    }

    public List<EmployeeResponse> convertEntityToDTO(List<EmployeeEntity> employeeEntities){
        if (employeeEntities==null){
            return Collections.emptyList();
        }

        return employeeEntities.stream().map(this::convertSingleEntityToDTO).toList();
    }

    public EmployeeResponse convertSingleEntityToDTO(EmployeeEntity employeeEntity){
        try {
            return new EmployeeResponse(
                    employeeEntity.getEmployee_id(),
                    employeeEntity.getInstitution()!=null? employeeEntity.getInstitution().getInstitutionId():null,
                    employeeEntity.getName(),
                    employeeEntity.getSurName(),
                    employeeEntity.getLastname(),
                    employeeEntity.getPosition(),
                    employeeEntity.getNonTaxable(),
                    employeeEntity.getSalary(),
                    employeeEntity.getBankAccount(),
                    employeeEntity.getEmail(),
                    employeeEntity.getActive(),
                    employeeEntity.getDeletedAt()
            );
        } catch (Exception e) {
            System.err.println("Ошибка конвертации Entity в DTO" + e.getMessage());
            return null;        }

    }
}

