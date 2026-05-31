package com.example.statement.service.converter;

import com.example.statement.dto.response.PayrollResponse;
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
                    payroll.getId(),
                    payroll.getInstitution().getId(),
                    payroll.getTotalIncome(),
                    payroll.getTotalUnionFee(),
                    payroll.getTotalIncomeTax(),
                    payroll.getTotalAdvance(),
                    payroll.getTotalIssued(),
                    DateFormatterUtil.getPeriodName(payroll.getMonth(), payroll.getYear()),
                    payroll.getPaymentDate(),
                    payroll.getCreatedAt(),
                    payroll.getUpdatedAt()
            );
        } catch (Exception e) {
            System.err.println("Ошибка конвертации сущности в DTO" + e.getMessage());
            return null;
        }
    }


}
