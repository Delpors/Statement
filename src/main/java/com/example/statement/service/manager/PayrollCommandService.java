package com.example.statement.service.manager;

import com.example.statement.dto.request.PayrollItemRequest;
import com.example.statement.dto.respons.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.PayrollEntity;
import com.example.statement.entity.PayrollItemsEntity;
import com.example.statement.repository.InstitutionRepository;
import com.example.statement.repository.PayrollItemsRepository;
import com.example.statement.repository.PayrollRepository;
import com.example.statement.service.converter.PayrollItemConverter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PayrollCommandService {

    private final PayrollRepository payrollRepository;
    private final PayrollItemsRepository payrollItemsRepository;
    private final PayrollItemConverter payrollItemConverter;
    private final InstitutionRepository institutionRepository;

    public PayrollCommandService(
            PayrollRepository payrollRepository,
            PayrollItemsRepository payrollItemsRepository,
            PayrollItemConverter payrollItemConverter,
            InstitutionRepository institutionRepository){

        this.payrollRepository = payrollRepository;
        this.payrollItemsRepository = payrollItemsRepository;
        this.payrollItemConverter = payrollItemConverter;
        this.institutionRepository = institutionRepository;
    }
    @Transactional
    public void createOrUpdatePayroll(
            LocalDate payrollDate,
            List<PayrollItemRequest> requests,
            Long institutionId){


        InstitutionEntity institution = institutionRepository.findById(institutionId).
                orElseThrow(()->new NoSuchElementException("Не найдена организация с id: " + institutionId));

        List<PayrollItemsEntity> items = payrollItemConverter.toEntity(requests, institution);

        PayrollEntity payroll = payrollRepository
                .findByPaymentDateAndInstitution(payrollDate, institution)
                .orElseGet(()->createPayroll(payrollDate, items, institution));

        updatePayrollItems(payroll, items);
        payrollRepository.save(payroll);
    }

    @Transactional
    public PayrollEntity createPayroll(
            LocalDate payrollDate,
            List<PayrollItemsEntity> items,
            InstitutionEntity institution) {

        PayrollEntity payroll = new PayrollEntity();

        payroll.setPaymentDate(payrollDate);
        payroll.setInstitution(institution);

        items.forEach(item->item.setPayroll(payroll));
        payroll.setItems(items);

        return payroll;
    }

    @Transactional
    public void updatePayrollItems(
            PayrollEntity payroll,
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
                updateEntityFields(existingEntity, newItemsEntity);
            } else {
                newItemsEntity.setPayroll(payroll);
                payroll.getItems().add(newItemsEntity);
            }
        }
    }

    public void updateEntityFields (
            PayrollItemsEntity existingEntity,
            PayrollItemsEntity newEntity) {
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

    @Transactional
    public void deletePayrollItem(Long id){

        try {
            payrollItemsRepository.deleteByPayrollItemId(id);
        }catch (EmptyResultDataAccessException e){
            throw new NoSuchElementException("Не найдена строка в ведомости с id: "+id);
        }
    }

    @Transactional
    public void deletePayroll(Long id){
        payrollRepository.deleteById(id);
    }
}
