package com.example.statement.service;

import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.response.EmployeeResponse;

import java.util.List;

public interface IEmployeeService {

    EmployeeResponse getEmployeeById(Long emplId);
    List<EmployeeResponse> getAllActiveEmployeesByInstitutionId(Long instId);
    void createEmployeeForInstitution(EmployeeRequest request, Long instId);
    void updateEmployee(Long emplId, EmployeeRequest request, Long instId);
    void deleteEmployee(Long emplId);
    Long getEmployeesCount(Long instId);
}
