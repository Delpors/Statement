package com.example.statement.service.manager;


import com.example.statement.dto.request.PayrollItemRequest;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.PayrollEntity;
import com.example.statement.entity.PayrollItemsEntity;

import java.time.LocalDate;
import java.util.List;

public interface IPayrollCommandService {

    void createOrUpdatePayroll(
            List<PayrollItemRequest> requests,
            Long institutionId);

    PayrollEntity createPayroll(
            LocalDate payrollDate,
            List<PayrollItemsEntity> items,
            InstitutionEntity institution);

    void updatePayrollItems(
            PayrollEntity payroll,
            List<PayrollItemsEntity> newEntities);

    void updateEntityFields (
            PayrollItemsEntity existingEntity,
            PayrollItemsEntity newEntity);

    void deletePayrollItem(Long id);

    void deletePayroll(Long id);

}
