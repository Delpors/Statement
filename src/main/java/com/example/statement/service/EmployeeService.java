package com.example.statement.service;

import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.respons.EmployeeResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.repository.EmployeeRepository;
import com.example.statement.service.converter.EmployeeConverter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final InstitutionService institutionService;
    private final EmployeeConverter employeeConverter;

    public EmployeeService(EmployeeRepository repository,
                           InstitutionService institutionService,
                           EmployeeConverter employeeConverter) {
        this.repository = repository;
        this.institutionService = institutionService;
        this.employeeConverter = employeeConverter;
    }

    public EmployeeResponse getEmployeeDTOById(Long id) {

        EmployeeEntity employee = repository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Не найден сотрудник с id " + id));
        return employeeConverter.toSingleResponse(employee);
    }

    public List<EmployeeResponse> getAllEmployees(Long instId) {
        List<EmployeeEntity> employeeEntities = repository.findActiveByInstitutionId(instId)
                .orElseThrow(()-> new NoSuchElementException("Сотрудники не найдены для организации с id: "+ instId));

        return employeeConverter.toResponse(employeeEntities);
    }

    public void createEmployee(EmployeeRequest request, Long instId) {

        InstitutionEntity institutionEntity = institutionService.getInstitutionEntityById(instId);
        repository.save(employeeConverter.toSingleEntity(request, institutionEntity));
    }

    public void updateEmployee(EmployeeRequest request, Long instId) {

        InstitutionEntity institutionEntity = institutionService.getInstitutionEntityById(instId);
        repository.save(employeeConverter.toSingleEntity(request, institutionEntity));
    }

    public void deleteEmployee(Long id) {

        EmployeeEntity employeeToDelete = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Сотрудник не найден"));

        employeeToDelete.softDelete();
        repository.save(employeeToDelete);
    }

    public int getEmployeesCount(InstitutionEntity institution){
        return repository.countAllByInstitutionAndActiveTrue(institution);
    }
}

