package com.example.statement.service;

import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.response.EmployeeResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.exceptions.EmployeeNotFoundException;
import com.example.statement.repository.EmployeeRepository;
import com.example.statement.service.converter.EmployeeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final InstitutionService institutionService;
    private final EmployeeConverter employeeConverter;

    public EmployeeResponse getEmployeeById(Long emplId) {

        EmployeeEntity employee = repository.findById(emplId)
                .orElseThrow(()->new EmployeeNotFoundException("Не найден сотрудник с id " + emplId));
        return employeeConverter.toSingleResponse(employee);
    }

    public List<EmployeeResponse> getAllActiveEmployeesByInstitutionId(Long instId) {

        List<EmployeeEntity> employeeEntities = repository.findActiveByInstId(instId);
        return employeeConverter.toResponse(employeeEntities);
    }

    @Transactional
    public void createEmployeeForInstitution(EmployeeRequest request, Long instId) {

        if (request==null){
            throw new IllegalArgumentException("Сведения о сотруднике не могут быть пустыми");
        }

        InstitutionEntity institutionEntity = institutionService.getInstitutionEntityById(instId);

        repository.save(employeeConverter.toSingleEntity(request, institutionEntity));
    }

    @Transactional
    public void updateEmployee(Long emplId, EmployeeRequest request, Long instId) {

        if (request==null){
            throw new IllegalArgumentException("Сведения о сотруднике не могут быть пустыми");
        }

        EmployeeEntity existingEmpl = repository
                .findByEmployeeIdAndInstId(emplId, instId)
                .orElseThrow(()-> new EmployeeNotFoundException("Сотрудник не найден."));

        existingEmpl.setName(request.name());
        existingEmpl.setSurName(request.surName());
        existingEmpl.setLastname(request.lastname());
        existingEmpl.setPosition(request.position());
        existingEmpl.setNonTaxable(request.nonTaxable());
        existingEmpl.setSalary(request.salary());
        existingEmpl.setBankAccount(request.bankAccount());
        existingEmpl.setEmail(request.email());
    }

    @Transactional
    public void deleteEmployee(Long emplId) {

        EmployeeEntity employeeToDelete = repository.findById(emplId)
                .orElseThrow(()-> new EmployeeNotFoundException("Сотрудник не найден"));

        employeeToDelete.softDelete();
        repository.save(employeeToDelete);
    }

    public long getEmployeesCount(Long emplId){
        return repository.countAllByInstitutionAndActiveTrue(emplId);
    }
}

