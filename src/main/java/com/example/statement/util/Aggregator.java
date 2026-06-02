package com.example.statement.util;

import com.example.statement.dto.response.MonthlyPayrollData;
import com.example.statement.dto.response.PayrollItemsResponse;
import com.example.statement.dto.response.ReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;

public class Aggregator {

    public static Page<ReportResponse> payrollItems(Page<PayrollItemsResponse> yearSalaryResponse,
                                                     Pageable pageable)
    {
        Map<Long, ReportResponse> summaryMap = new LinkedHashMap<>();

        yearSalaryResponse.getContent().forEach(item ->{
            Long employeeId = item.employeeId();
            ReportResponse summary = summaryMap.computeIfAbsent(employeeId, id ->{
                ReportResponse newSummary = new ReportResponse();

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

        List<ReportResponse> summaryResponses = new ArrayList<>(summaryMap.values());

        int totalEmployees = summaryMap.size();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

        int fromIndex = Math.min(currentPage * pageSize, summaryResponses.size());
        int toIndex = Math.min((currentPage + 1) * pageSize, summaryResponses.size());

        List<ReportResponse> pagedList = summaryResponses.subList(fromIndex, toIndex);

        return new PageImpl<>(
                pagedList,
                pageable,
                totalEmployees
                );
    }
}
