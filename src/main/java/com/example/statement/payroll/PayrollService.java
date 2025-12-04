package com.example.statement.payroll;

import com.example.statement.employees.EmployeeService;
import com.example.statement.institution.InstitutionEntity;
import com.example.statement.institution.InstitutionService;
import com.example.statement.payroll_items.PayrollItemsDTO;
import com.example.statement.payroll_items.PayrollItemsEntity;
import com.example.statement.payroll_items.PayrollItemsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PayrollService {

    PayrollRepository payrollRepository;
    PayrollItemsService payrollItemsService;
    EmployeeService employeeService;
    InstitutionService institutionService;

    public PayrollService(PayrollRepository payrollRepository,
                          PayrollItemsService payrollItemsService,
                          EmployeeService employeeService,
                          InstitutionService institutionService
    ) {
        this.payrollRepository = payrollRepository;
        this.payrollItemsService = payrollItemsService;
        this.employeeService = employeeService;
        this.institutionService = institutionService;
    }

    public List<PayrollItemsDTO> createPayrollItems(Long instId){

        return payrollItemsService.getAllPayrollItemsToCreate(instId);
    }

    public Page<PayrollItemsDTO> getPayrollItems(Long payrollId, Long selectedInst, Pageable pageable){
        return payrollItemsService.getPayrollItemsByPayrollId(payrollId, selectedInst, pageable);
    }

    public void createOrUpdatePayroll(LocalDate payrollData,
                                      List<PayrollItemsDTO> itemsDTOS,
                                      Long instId){

        if (itemsDTOS==null || itemsDTOS.isEmpty()){
            throw new IllegalArgumentException("Ведомость не может быть пустой");
        }

        boolean isExist = payrollRepository.existsByPaymentDate(payrollData);

        if (isExist){
            updatePayroll(payrollData, itemsDTOS, instId);
        }else {
            createPayroll(payrollData, itemsDTOS, instId);
        }
    }

    public void createPayroll(LocalDate payrollData,
                              List<PayrollItemsDTO> payrollItemsDTOS,
                              Long instId) {
        PayrollEntity payroll = new PayrollEntity();
        InstitutionEntity institutionEntity = institutionService.getInstitutionEntityById(instId);

        payroll.setPaymentDate(payrollData);
        payroll.setInstitution(institutionEntity);

        List<PayrollItemsEntity> items = payrollItemsService.convertDTOtoEntity(payrollItemsDTOS, institutionEntity);
        items.forEach(item->item.setPayroll(payroll));

        payroll.setItems(items);
        payroll.calculateTotals();

        payrollRepository.save(payroll);
    }

    public void updatePayroll(LocalDate payrollData,
                              List<PayrollItemsDTO> payrollItems,
                              Long instId) {
        InstitutionEntity institutionEntity = institutionService.getInstitutionEntityById(instId);

        PayrollEntity payroll = payrollRepository
                    .findByPaymentDate(payrollData)
                    .orElseThrow(()-> new RuntimeException("Ведомость не найдена"));

        List<PayrollItemsEntity> newItems = payrollItemsService.convertDTOtoEntity(payrollItems, institutionEntity);

        updatePayrollItems(payroll,newItems);
        payroll.calculateTotals();
        payrollRepository.save(payroll);

    }

    public void updatePayrollItems(PayrollEntity payroll,
                                   List<PayrollItemsEntity> newEntities) {

        Function<PayrollItemsEntity, String> keyFunc = entity ->
                entity.getEmployee().getEmployee_id() + "_" + entity.getPaymentDate();

        Set<String> newKeys = newEntities.stream()
                .map(keyFunc)
                .collect(Collectors.toSet());

        payroll.getItems().removeIf(existingItem ->
                !newKeys.contains(keyFunc.apply(existingItem)));

        Map<String, PayrollItemsEntity> existingMap = payroll.getItems().stream()
                .collect(Collectors.toMap(
                        keyFunc,
                        Function.identity()
                ));

        for (PayrollItemsEntity newItemsEntity : newEntities) {
            String key = keyFunc.apply(newItemsEntity);

            if (existingMap.containsKey(key)) {
                PayrollItemsEntity existingEntity = existingMap.get(key);
                payrollItemsService.updateEntityFields(existingEntity, newItemsEntity);
            } else {
                newItemsEntity.setPayroll(payroll);
                payroll.getItems().add(newItemsEntity);
            }
        }
    }

    public List<PayrollDTO> getAllPayrolls (Long instId){

        List<PayrollEntity> payrollEntity = payrollRepository.findAllByInstitutionId(instId);

        return convertPayrollEntityToDTO(payrollEntity);
    }

    public PayrollDTO getPayrollByDate(LocalDate payrollData){
        PayrollEntity payroll = payrollRepository.findByPaymentDate(payrollData)
                .orElseThrow(()->new RuntimeException("Ведомость на эту дату не найдена"));

        return convertSinglePayroll(payroll);
    }

    public void deletePayroll(Long id){
        if (!payrollRepository.existsById(id)){
            throw new NoSuchElementException("Не найдена ведомость с id" + id);
        }
        payrollRepository.deleteById(id);
    }

    public void deletePayrollItem(Long id){

        payrollItemsService.deletePayrollItem(id);
    }

    public List<PayrollDTO> convertPayrollEntityToDTO(List<PayrollEntity> payrollEntity) {

        if (payrollEntity==null){
            return Collections.emptyList();
        }

        return payrollEntity.stream().map(this::convertSinglePayroll).toList();

    }

    public PayrollDTO convertSinglePayroll(PayrollEntity payroll) {
        try {
            return new PayrollDTO(
                    payroll.getPayrollid(),
                    payroll.getItems().getFirst().getPayrollItemId(),
                    payroll.getInstitution().getInstitutionId(),
                    payroll.getTotalIncome(),
                    payroll.getTotalUnionFee(),
                    payroll.getTotalIncomeTax(),
                    payroll.getTotalAdvance(),
                    payroll.getTotalIssued(),
                    payroll.getPaymentDate(),
                    payroll.getCreatedAt(),
                    payroll.getStatus().name()
            );
        } catch (Exception e) {
            System.err.println("Ошибка конвертации сушности в DTO" + e.getMessage());
            return null;
        }

    }
}
