package com.example.statement.service;

import com.example.statement.dto.response.*;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.PayrollEntity;
import com.example.statement.entity.PayrollItemsEntity;
import com.example.statement.repository.EmployeeRepository;
import com.example.statement.repository.InstitutionRepository;
import com.example.statement.repository.PayrollItemsRepository;
import com.example.statement.repository.PayrollRepository;
import com.example.statement.util.Aggregator;
import com.example.statement.service.converter.EmployeeConverter;
import com.example.statement.service.converter.PayrollConverter;
import com.example.statement.service.converter.PayrollItemConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@Validated
@RequiredArgsConstructor
public class PayrollQueryService implements IPayrollQueryService {

    private final PayrollRepository payrollRepository;
    private final PayrollItemsRepository payrollItemsRepository;
    private final PayrollItemConverter payrollItemConverter;
    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;
    private final InstitutionRepository institutionRepository;

    @Transactional(readOnly = true)
    public Page<PayrollItemsResponse> getPayrollItems(Long payrollId, Long selectedInst, Pageable pageable)
    {

        Page<PayrollItemsEntity> payrollItemsEntityPage;

        payrollItemsEntityPage = payrollItemsRepository
                .getAllByPayrollItemIdAndInstitutionId(payrollId, selectedInst, pageable);

        return payrollItemConverter.toResponse(payrollItemsEntityPage);
    }

    @Transactional(readOnly = true)
    public List<PayrollResponse> getAllPayrolls (Long instId)
    {
        List<PayrollEntity> payrollEntity = payrollRepository.findAllByInstitutionId(instId)
                .orElseThrow(()-> new NoSuchElementException("Не найдена организация с Id"+ instId));


        return new PayrollConverter().toResponse(payrollEntity);
    }

    @Transactional(readOnly = true)
    public List<DataToCreatePayroll> getIEmployeeItems(Long instId)
    {

        return employeeConverter
                .toCreatePayrollResponse(employeeRepository
                        .findActiveByInstId(instId));
    }


    @Transactional(readOnly = true)
    public Page<ReportResponse> getEmployeesYearSalary(Integer year, Long institutionId, Pageable pageable)
    {
        InstitutionEntity institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new NoSuchElementException("Организация не найдена"));

        Page<PayrollItemsEntity> payrollItems = payrollItemsRepository
                .findAllByYearAndInstitution(year, institution, pageable);

        Page<PayrollItemsResponse> yearSalaryResponse = payrollItemConverter.toResponse(payrollItems);

        return  Aggregator.payrollItems(yearSalaryResponse, pageable);
    }

    public long getCount(InstitutionEntity institution)
    {
        return payrollRepository.countAllByInstitution(institution);
    }
}
