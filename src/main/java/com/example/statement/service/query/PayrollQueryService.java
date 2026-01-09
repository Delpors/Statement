package com.example.statement.service.query;

import com.example.statement.dto.respons.*;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.PayrollEntity;
import com.example.statement.entity.PayrollItemsEntity;
import com.example.statement.repository.EmployeeRepository;
import com.example.statement.repository.InstitutionRepository;
import com.example.statement.repository.PayrollItemsRepository;
import com.example.statement.repository.PayrollRepository;
import com.example.statement.service.Aggregator;
import com.example.statement.service.converter.EmployeeConverter;
import com.example.statement.service.converter.PayrollConverter;
import com.example.statement.service.converter.PayrollItemConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.spel.spi.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class PayrollQueryService {
    private final PayrollRepository payrollRepository;
    private final PayrollItemsRepository payrollItemsRepository;
    private final PayrollItemConverter payrollItemConverter;
    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;
    private final InstitutionRepository institutionRepository;

    public PayrollQueryService(PayrollRepository payrollRepository,
                               PayrollItemsRepository payrollItemsRepository,
                               PayrollItemConverter payrollItemConverter, EmployeeRepository employeeRepository, EmployeeConverter employeeConverter, InstitutionRepository institutionRepository)
    {
        this.payrollRepository = payrollRepository;
        this.payrollItemsRepository = payrollItemsRepository;
        this.payrollItemConverter = payrollItemConverter;
        this.employeeRepository = employeeRepository;
        this.employeeConverter = employeeConverter;
        this.institutionRepository = institutionRepository;
    }

    @Transactional(readOnly = true)
    public Page<PayrollItemsResponse> getPayrollItems(Long payrollId, Long selectedInst, Pageable pageable){

        Page<PayrollItemsEntity> payrollItemsEntityPage;

        payrollItemsEntityPage = payrollItemsRepository
                .getAllByPayrollItemIdAndInstitutionId(payrollId, selectedInst, pageable);

        return payrollItemConverter.toResponse(payrollItemsEntityPage);
    }

    @Transactional(readOnly = true)
    public List<PayrollResponse> getAllPayrolls (
            Long instId)
    {
        List<PayrollEntity> payrollEntity = payrollRepository.findAllByInstitutionId(instId)
                .orElseThrow(()-> new NoSuchElementException("Не найдена организация с Id"+ instId));


        return new PayrollConverter().toResponse(payrollEntity);
    }

    @Transactional(readOnly = true)
    public List<DataToCreatePayroll> getIEmployeeItems(Long instId){

        return employeeConverter
                .toCreatePayrollResponse(employeeRepository
                        .findActiveByInstitutionId(instId)
                        .orElseThrow(()-> new NoSuchElementException("Не найдены сотрудники для организации с Id:" + instId)));
    }


    @Transactional(readOnly = true)
    public Page<PayrollSummaryResponse> getEmployeesYearSalary(Integer year, Long institutionId, Pageable pageable) {
        InstitutionEntity institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new NoSuchElementException("Организация не найдена"));

        Page<PayrollItemsEntity> payrollItems = payrollItemsRepository
                .findAllByYearAndInstitution(year, institution, pageable);

        Page<PayrollItemsResponse> yearSalaryResponse = payrollItemConverter.toResponse(payrollItems);

        return  Aggregator.payrollItems(yearSalaryResponse, pageable);
    }
}
