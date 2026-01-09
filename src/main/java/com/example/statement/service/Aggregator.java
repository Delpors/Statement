package com.example.statement.service;

import com.example.statement.dto.respons.MonthlyPayrollData;
import com.example.statement.dto.respons.PayrollItemsResponse;
import com.example.statement.dto.respons.PayrollSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;

public class Aggregator {

    public static Page<PayrollSummaryResponse> payrollItems(Page<PayrollItemsResponse> yearSalaryResponse,
                                                     Pageable pageable)
    {
        Map<Long, PayrollSummaryResponse> summaryMap = new LinkedHashMap<>();

        yearSalaryResponse.getContent().forEach(item ->{
            Long employeeId = item.employeeId();
            PayrollSummaryResponse summary = summaryMap.computeIfAbsent(employeeId, id ->{
                PayrollSummaryResponse newSummary = new PayrollSummaryResponse();

                newSummary.setEmployeeId(item.employeeId());
                newSummary.setFullName(item.fullName());
                newSummary.setMonthlyData(new TreeMap<>());
                newSummary.setTotalSalary(BigDecimal.ZERO);
                newSummary.setTotalTax(BigDecimal.ZERO);
                newSummary.setTotalUnionFee(BigDecimal.ZERO);

                return newSummary;
            });

            String monthKay = String.format("%s-%02d", item.year(), item.month());
            MonthlyPayrollData monthly = new MonthlyPayrollData(
                    item.baseSalary(),
                    item.incomeTax(),
                    item.unionFee()
            );

            summary.getMonthlyData().put(monthKay,monthly);
            summary.setTotalSalary(summary.getTotalSalary().add(item.baseSalary()));
            summary.setTotalTax(summary.getTotalTax().add(item.incomeTax()));
            summary.setTotalUnionFee(summary.getTotalUnionFee().add(item.unionFee()));
        });

        List<PayrollSummaryResponse> summaryResponses = new ArrayList<>(summaryMap.values());


        int totalEmployees = summaryMap.size();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();


        int fromIndex = Math.min(currentPage * pageSize, summaryResponses.size());
        int toIndex = Math.min((currentPage + 1) * pageSize, summaryResponses.size());

        List<PayrollSummaryResponse> pagedList = summaryResponses.subList(fromIndex, toIndex);

        return new PageImpl<>(
                pagedList,
                pageable,
                totalEmployees
                );
    }
}
