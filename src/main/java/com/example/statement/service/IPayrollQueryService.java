package com.example.statement.service;

import com.example.statement.dto.response.DataToCreatePayroll;
import com.example.statement.dto.response.PayrollItemsResponse;
import com.example.statement.dto.response.PayrollResponse;
import com.example.statement.dto.response.ReportResponse;
import com.example.statement.entity.InstitutionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPayrollQueryService {

    Page<PayrollItemsResponse> getPayrollItems(Long payrollId, Long selectedInst, Pageable pageable);
    List<PayrollResponse> getAllPayrolls (Long instId);
    List<DataToCreatePayroll> getIEmployeeItems(Long instId);
    Page<ReportResponse> getEmployeesYearSalary(Integer year, Long institutionId, Pageable pageable);
    long getCount(InstitutionEntity institution);
}
