package com.example.statement.service;

import com.example.statement.repository.PayrollItemsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PayrollItemsService {

    PayrollItemsRepository payrollItemsRepository;
    EmployeeService employeeService;

    public PayrollItemsService(PayrollItemsRepository payrollItemsRepository,
                               EmployeeService employeeService
    ){
        this.payrollItemsRepository = payrollItemsRepository;
        this.employeeService = employeeService;
    }

/*    public List<DataToCreatePayroll> getAllPayrollItemsToCreate(Long selectedInst) {

        List<EmployeeEntity> employees = employeeService.getAllEmployeeEntities(selectedInst);
        return convertEmployeesToPayrollDTO(employees);
    }*/

/*    public void updateEntityFields (PayrollItemsEntity existingEntity,PayrollItemsEntity newEntity) {
        existingEntity.setEmployee(newEntity.getEmployee());
        existingEntity.setBaseSalary(newEntity.getBaseSalary());
        existingEntity.setBonus(newEntity.getBonus());
        existingEntity.setFss(newEntity.getFss());
        existingEntity.setReplace(newEntity.getReplace());
        existingEntity.setOtherIncome(newEntity.getOtherIncome());
        existingEntity.setTotalEmployeeIncome(newEntity.getTotalEmployeeIncome());
        existingEntity.setAbsent(newEntity.getAbsent());
        existingEntity.setUnionFee(newEntity.getUnionFee());
        existingEntity.setIncomeTax(newEntity.getIncomeTax());
        existingEntity.setAdvance(newEntity.getAdvance());
        existingEntity.setTotalEmployeeDeduction(newEntity.getTotalEmployeeDeduction());
        existingEntity.setTotalIssued(newEntity.getTotalIssued());
        existingEntity.setPaymentDate(newEntity.getPaymentDate());
    }*/

/*
    public void deletePayrollItem(Long payrollItemId){
        payrollItemsRepository.deleteByPayrollItemId(payrollItemId);
    }
*/

}


