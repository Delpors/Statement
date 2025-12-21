package com.example.statement.service.converter;

import com.example.statement.dto.respons.PayrollResponse;
import com.example.statement.entity.PayrollEntity;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


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
                    getPeriodName(payroll.getMonth(), payroll.getYear()),
                    payroll.getPaymentDate(),
                    payroll.getCreatedAt()
            );
        } catch (Exception e) {
            System.err.println("Ошибка конвертации сущности в DTO" + e.getMessage());
            return null;
        }
    }

    private String getPeriodName(Integer month, Integer year)
    {
        String monthName = Month.of(month).getDisplayName(TextStyle.FULL, new Locale("ru"));
        return Character.toUpperCase(monthName.charAt(0)) + monthName.substring(1) + " " + year;
    }
}
