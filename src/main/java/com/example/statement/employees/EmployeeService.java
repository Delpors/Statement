package com.example.statement.employees;

import com.example.statement.institution.InstitutionDTO;
import com.example.statement.institution.InstitutionEntity;
import com.example.statement.institution.InstitutionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public EmployeeDTO getEmployeeDTOById(Long id) {

        EmployeeEntity employee = repository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Не найден сотрудник с id " + id));
        return convertSingleEntityToDTO(employee);
    }

    public List<EmployeeDTO> getAllEmployees(Long instId) {
        List<EmployeeEntity> employeeEntities = repository.findActiveByInstitutionId(instId);

        return convertEntityToDTO(employeeEntities);
    }

    public List<EmployeeEntity> getAllEmployeeEntities(Long instId) {

        return repository.findActiveByInstitutionId(instId);
    }

    public void createEmployee(EmployeeDTO employeeDTO, Long instId) {

        if (employeeDTO.employee_id()!=null){
            throw new IllegalArgumentException("Id should be empty");
        }

        InstitutionEntity institutionEntity = institutionService.getInstitutionEntityById(instId);
        repository.save(convertSingleEmployee(employeeDTO, institutionEntity));
    }

    public void updateEmployee(EmployeeDTO employeeDTOToUpdate, Long instId) {


        InstitutionEntity institutionEntity = institutionService.getInstitutionEntityById(instId);
        repository.save(convertSingleEmployee(employeeDTOToUpdate, institutionEntity));
    }

    public void deleteEmployee(Long id) {

        EmployeeEntity employeeToDelete = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Сотрудник не найден"));

        employeeToDelete.softDelete();
        repository.save(employeeToDelete);
    }

    public EmployeeEntity convertSingleEmployee(EmployeeDTO employeeDTO, InstitutionEntity institution){

        try {
            return new EmployeeEntity(
                    employeeDTO.employee_id() !=null ? employeeDTO.employee_id() : null,
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

    public List<EmployeeDTO> convertEntityToDTO(List<EmployeeEntity> employeeEntities){
        if (employeeEntities==null){
            return Collections.emptyList();
        }

        return employeeEntities.stream().map(this::convertSingleEntityToDTO).toList();
    }

    public EmployeeDTO convertSingleEntityToDTO(EmployeeEntity employeeEntity){
        try {
            return new EmployeeDTO(
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

