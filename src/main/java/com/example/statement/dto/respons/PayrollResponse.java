package com.example.statement.dto.respons;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PayrollResponse(

        Long payrollId,
        Long institutionId,
        BigDecimal totalIncome,
        BigDecimal totalUnionFee,
        BigDecimal totalIncomeTax,
        BigDecimal totalAdvance,
        BigDecimal totalIssued,
        String period,
        LocalDate payrollDate,
        LocalDateTime createdAt
){

}
