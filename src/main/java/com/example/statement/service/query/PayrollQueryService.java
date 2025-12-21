package com.example.statement.service.query;

import com.example.statement.dto.respons.DataToCreatePayroll;
import com.example.statement.dto.respons.PayrollItemsResponse;
import com.example.statement.dto.respons.PayrollResponse;
import com.example.statement.entity.PayrollEntity;
import com.example.statement.entity.PayrollItemsEntity;
import com.example.statement.repository.EmployeeRepository;
import com.example.statement.repository.PayrollItemsRepository;
import com.example.statement.repository.PayrollRepository;
import com.example.statement.service.converter.EmployeeConverter;
import com.example.statement.service.converter.PayrollConverter;
import com.example.statement.service.converter.PayrollItemConverter;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Validated
public class PayrollQueryService {
    private final PayrollRepository payrollRepository;
    private final PayrollItemsRepository payrollItemsRepository;
    private final PayrollItemConverter payrollItemConverter;
    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;

    public PayrollQueryService(PayrollRepository payrollRepository,
                               PayrollItemsRepository payrollItemsRepository,
                               PayrollItemConverter payrollItemConverter, EmployeeRepository employeeRepository, EmployeeConverter employeeConverter)
    {
        this.payrollRepository = payrollRepository;
        this.payrollItemsRepository = payrollItemsRepository;
        this.payrollItemConverter = payrollItemConverter;
        this.employeeRepository = employeeRepository;
        this.employeeConverter = employeeConverter;
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
}
