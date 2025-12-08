package com.example.statement.service;

import com.example.statement.dto.respons.PayrollItemsResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.entity.PayrollItemsEntity;
import com.example.statement.repository.PayrollItemsRepository;
import com.example.statement.entity.InstitutionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    public Page<PayrollItemsResponse> getPayrollItemsByPayrollId (Long payrollId, Long selectedInst, Pageable pageable){
        Page<PayrollItemsEntity> payrollItemsByDate;
        payrollItemsByDate = payrollItemsRepository.getAllByPayrollItemIdAndInstitutionId(payrollId,selectedInst, pageable);

        return convertEntityToDTOtoEdit(payrollItemsByDate);
    }

    public List<PayrollItemsResponse> getAllPayrollItemsToCreate(Long selectedInst) {

        List<EmployeeEntity> employees = employeeService.getAllEmployeeEntities(selectedInst);
        return convertEmployeesToPayrollDTO(employees);
    }

    public void updateEntityFields (PayrollItemsEntity existingEntity,PayrollItemsEntity newEntity) {
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
    }

    public List<PayrollItemsEntity> convertDTOtoEntity (List<PayrollItemsResponse> payrollItemsDTO,
                                                        InstitutionEntity institution){

        return payrollItemsDTO.stream().map(

                dto-> {
                    PayrollItemsEntity entity = new PayrollItemsEntity();

                    if (dto.payrollItemId() != null) {
                        entity.setPayrollItemId(dto.payrollItemId());
                    }

                    EmployeeEntity employee = employeeService.getEmployeeById(dto.employeeId());
                    entity.setInstitution(institution);
                    entity.setEmployee(employee);
                    entity.setBaseSalary(dto.baseSalary());
                    entity.setBonus(dto.bonus());
                    entity.setFss(dto.fss());
                    entity.setReplace(dto.replace());
                    entity.setOtherIncome(dto.otherIncome());
                    entity.setTotalEmployeeIncome(dto.totalEmployeeIncome());
                    entity.setAbsent(dto.absent());
                    entity.setUnionFee(dto.unionFee());
                    entity.setIncomeTax(dto.incomeTax());
                    entity.setAdvance(dto.advance());
                    entity.setTotalEmployeeDeduction(dto.totalEmployeeDeduction());
                    entity.setTotalIssued(dto.totalIssued());
                    entity.setPaymentDate(dto.paymentDate());

                    return entity;
                }).toList();
    }

    public void deletePayrollItem(Long payrollItemId){
        payrollItemsRepository.deleteByPayrollItemId(payrollItemId);
    }

    private List<PayrollItemsResponse> convertEmployeesToPayrollDTO(List<EmployeeEntity> employees) {
        return employees.stream()
                .map(employee -> new PayrollItemsResponse(
                        null, // payrollItemId - пока нет записи
                        employee.getEmployee_id(),
                        employee.getInstitution().getInstitutionId(),
                        employee.getSurName() + " " + employee.getName() + " " + employee.getLastname(),
                        employee.getNonTaxable(),
                        employee.getPosition(),
                        employee.getSalary(), // если оклад хранится в employee
                        BigDecimal.ZERO, // bonus - начальные значения
                        BigDecimal.ZERO, // fss
                        BigDecimal.ZERO, // replace
                        BigDecimal.ZERO, // otherIncome
                        BigDecimal.ZERO, // totalEmployeeIncome
                        BigDecimal.ZERO, // absent
                        BigDecimal.ZERO, // unionFee
                        BigDecimal.ZERO, // incomeTax
                        BigDecimal.ZERO, // advance
                        BigDecimal.ZERO, // totalEmployeeDeduction
                        BigDecimal.ZERO, // totalIssued
                        null, // createdAt
                        null // paymentData
                ))
                .collect(Collectors.toList());
    }

    private Page<PayrollItemsResponse> convertEntityToDTOtoEdit(Page<PayrollItemsEntity> payrollItemsEntities) {
        List<PayrollItemsResponse> dtos = payrollItemsEntities.stream()
                .map(item -> new PayrollItemsResponse(
                        item.getPayrollItemId(),
                        item.getEmployee().getEmployee_id(),
                        item.getInstitution().getInstitutionId(),
                        item.getEmployee().getSurName() + " " + item.getEmployee().getName() + " " + item.getEmployee().getLastname(),
                        item.getEmployee().getNonTaxable(),
                        item.getEmployee().getPosition(),
                        item.getEmployee().getSalary(),
                        item.getBonus(),
                        item.getFss(),
                        item.getReplace(),
                        item.getOtherIncome(),
                        item.getTotalEmployeeIncome(),
                        item.getAbsent(),
                        item.getUnionFee(),
                        item.getIncomeTax(),
                        item.getAdvance(),
                        item.getTotalEmployeeDeduction(),
                        item.getTotalIssued(),
                        item.getPaymentDate(),
                        item.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, payrollItemsEntities.getPageable(), payrollItemsEntities.getTotalElements());
    }
}


