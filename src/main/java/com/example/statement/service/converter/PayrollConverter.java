package com.example.statement.service.converter;

import com.example.statement.dto.respons.PayrollResponse;
import com.example.statement.entity.PayrollEntity;

import java.util.Collections;
import java.util.List;


public class PayrollConverter {

    public List<PayrollResponse> toResponse(List<PayrollEntity> payrollEntity) {

        if (payrollEntity==null){
            return Collections.emptyList();
        }

        return payrollEntity.stream().map(this::singleEntityToResponse).toList();
    }

    public PayrollResponse singleEntityToResponse(PayrollEntity payroll) {
        try {
            return new PayrollResponse(
                    payroll.getPayrollid(),
                    payroll.getInstitution().getInstitutionId(),
                    payroll.getTotalIncome(),
                    payroll.getTotalUnionFee(),
                    payroll.getTotalIncomeTax(),
                    payroll.getTotalAdvance(),
                    payroll.getTotalIssued(),
                    payroll.getPaymentDate(),
                    payroll.getCreatedAt()
            );
        } catch (Exception e) {
            System.err.println("Ошибка конвертации сушности в DTO" + e.getMessage());
            return null;
        }

    }
}
