package com.example.statement.service;

import com.example.statement.dto.request.PayrollItemRequest;
import com.example.statement.dto.respons.*;
import com.example.statement.repository.PayrollItemsRepository;
import com.example.statement.service.manager.PayrollCommandService;
import com.example.statement.service.query.PayrollQueryService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Service
@Validated
public class PayrollOrchestratorService {

    private final PayrollCommandService payrollCommandService;
    private final PayrollQueryService payrollQueryService;

    public PayrollOrchestratorService(PayrollCommandService payrollCommandService,
                                      PayrollQueryService payrollQueryService, PayrollItemsRepository payrollItemsRepository
    ) {

        this.payrollCommandService = payrollCommandService;
        this.payrollQueryService = payrollQueryService;
    }

    public void createOrUpdatePayroll(
            @NotEmpty (message = "Ведомость не может быть пустым")
            List<PayrollItemRequest> requests,

            @NotNull (message = "Id учреждения обязателен")
            @Min(value = 1, message = "Id учреждения должен быть положительным")
            Long institutionId)
    {
        payrollCommandService.createOrUpdatePayroll(requests, institutionId);
    }

    public List<DataToCreatePayroll> getItemsToCreatePayroll(
            @NotNull(message = "Id учреждения обязателен")
            @Min(value = 1, message = "Id учреждения должен быть положительным")
            Long institutionId)
    {
        return payrollQueryService.getIEmployeeItems(institutionId);
    }

    public List<PayrollResponse> getAllPayrolls(
            @NotNull(message = "Id учреждения обязателен")
            @Min(value = 1, message = "Id учреждения должен быть положительным")
            Long institutionId)
    {
        return payrollQueryService.getAllPayrolls(institutionId);
    }

    public Page<PayrollItemsResponse> getPayrollItems(
            @NotNull
            Long payrollId,
            @NotNull
            Long selectedInst,
            Pageable pageable)
    {
        return payrollQueryService.getPayrollItems(payrollId, selectedInst, pageable);
    }

    public void deletePayroll(
            @NotNull(message = "Id ведомости обязателен")
            Long payrollId)
    {
        payrollCommandService.deletePayroll(payrollId);
    }

    public void deletePayrollItem(
            @NotNull(message = "Id строки ведомости обязателен")
            Long payrollItemId)
    {
        payrollCommandService.deletePayrollItem(payrollItemId);
    }

    public Page<PayrollSummaryResponse> getEmployeesSalary(Integer year, Long institutionId, Pageable pageable)
    {
        return payrollQueryService.getEmployeesYearSalary(year, institutionId, pageable);
    }
}
