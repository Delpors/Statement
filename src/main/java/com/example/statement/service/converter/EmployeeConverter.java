package com.example.statement.service.converter;


import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.respons.DataToCreatePayroll;
import com.example.statement.dto.respons.EmployeeResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.entity.InstitutionEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeConverter {

    public List<DataToCreatePayroll> toCreatePayrollResponse(List<EmployeeEntity> employees) {
        return employees.stream()
                .map(employee -> new DataToCreatePayroll(
                        employee.getEmployeeId(),
                        employee.getInstitution().getInstitutionId(),
                        employee.getFullName(),
                        employee.getNonTaxable(),
                        employee.getPosition(),
                        employee.getSalary()

                ))
                .collect(Collectors.toList());
    }

    public EmployeeEntity toSingleEntity(EmployeeRequest employeeDTO, InstitutionEntity institution){

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

    public List<EmployeeResponse> toResponse(List<EmployeeEntity> employeeEntities){
        if (employeeEntities==null){
            return Collections.emptyList();
        }

        return employeeEntities.stream().map(this::toSingleResponse).toList();
    }

    public EmployeeResponse toSingleResponse(EmployeeEntity employeeEntity){
        try {
            return new EmployeeResponse(
                    employeeEntity.getEmployeeId(),
                    employeeEntity.getInstitution()!=null? employeeEntity.getInstitution().getInstitutionId():null,
                    employeeEntity.getSurName(),
                    employeeEntity.getName(),
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
