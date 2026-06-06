package com.example.statement.service;

import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.response.EmployeeResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.exceptions.EmployeeNotFoundException;
import com.example.statement.repository.EmployeeRepository;
import com.example.statement.service.converter.EmployeeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService{

    private final EmployeeRepository repository;
    private final IInstitutionService institutionService;
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
        log.info("Employee {} created", request.name());
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

        log.info("Employee {} updated", existingEmpl.getName());
    }

    @Transactional
    public void deleteEmployee(Long emplId) {

        EmployeeEntity employeeToDelete = repository.findById(emplId)
                .orElseThrow(()-> new EmployeeNotFoundException("Сотрудник не найден"));

        employeeToDelete.softDelete();
        repository.save(employeeToDelete);

        log.info("Employee {} deleted", emplId);
    }

    public long getEmployeesCount(Long instId){
        return repository.countAllByInstitutionAndActiveTrue(instId);
    }
}

