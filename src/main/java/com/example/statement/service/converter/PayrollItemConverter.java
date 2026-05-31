package com.example.statement.service.converter;

import com.example.statement.dto.request.PayrollItemRequest;
import com.example.statement.dto.response.PayrollItemsResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.PayrollItemsEntity;
import com.example.statement.exceptions.EmployeeNotFoundException;
import com.example.statement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PayrollItemConverter {
    private final EmployeeRepository employeeRepository;

    public List<PayrollItemsEntity> toEntity (List<PayrollItemRequest> requests,
                                              InstitutionEntity institution) {

        return requests.stream().map(

                dto-> {
                    PayrollItemsEntity entity = new PayrollItemsEntity();
                    EmployeeEntity employee = employeeRepository
                            .findByEmployeeIdAndInstId(dto.employeeId(), institution.getId())
                            .orElseThrow(()-> new EmployeeNotFoundException("Сотрудник не найден"));

                    entity.setEmployee(employee);
                    entity.setInstitution(institution);
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
                    entity.setMonth(dto.month());
                    entity.setYear(dto.year());
                    entity.setPaymentDate(dto.paymentDate());

                    return entity;
                }).toList();
    }
    public Page<PayrollItemsResponse> toResponse(Page<PayrollItemsEntity> payrollItemsEntities) {
        List<PayrollItemsResponse> dtos = payrollItemsEntities
                .stream()
                .map(item -> new PayrollItemsResponse(
                        item.getId(),
                        item.getEmployee().getId(),
                        item.getInstitution().getId(),
                        item.getEmployee().getFullName(),
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
                        DateFormatterUtil.getPeriodName(item.getMonth(), item.getYear()),
                        item.getMonth(),
                        item.getYear(),
                        item.getPaymentDate(),
                        item.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, payrollItemsEntities.getPageable(), payrollItemsEntities.getTotalElements());
    }

}
