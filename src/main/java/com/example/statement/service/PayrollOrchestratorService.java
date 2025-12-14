package com.example.statement.service;

import com.example.statement.dto.request.PayrollItemRequest;
import com.example.statement.dto.respons.DataToCreatePayroll;
import com.example.statement.dto.respons.PayrollItemsResponse;
import com.example.statement.dto.respons.PayrollResponse;
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


@Service
@Validated
public class PayrollOrchestratorService {

    private final PayrollCommandService payrollCommandService;
    private final PayrollQueryService payrollQueryService;

    public PayrollOrchestratorService(PayrollCommandService payrollCommandService,
                                      PayrollQueryService payrollQueryService
    ) {

        this.payrollCommandService = payrollCommandService;
        this.payrollQueryService = payrollQueryService;
    }

    public void createOrUpdatePayroll(
            @NotNull (message = "Дата ведомости обязательна")
            LocalDate payrollDate,

            @NotEmpty (message = "Ведомость не может быть пустым")
            List<PayrollItemRequest> requests,

            @NotNull (message = "Id учреждения обязателен")
            @Min(value = 1, message = "Id учреждения должен быть положительным")
            Long institutionId)
    {
        payrollCommandService.createOrUpdatePayroll(payrollDate, requests, institutionId);
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
            @NotEmpty
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
}
