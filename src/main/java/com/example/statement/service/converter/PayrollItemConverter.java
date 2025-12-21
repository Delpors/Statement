package com.example.statement.service.converter;

import com.example.statement.dto.request.PayrollItemRequest;
import com.example.statement.dto.respons.InstitutionResponse;
import com.example.statement.dto.respons.PayrollItemsResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.PayrollItemsEntity;
import com.example.statement.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PayrollItemConverter {
    private final EmployeeRepository employeeRepository;

    public PayrollItemConverter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<PayrollItemsEntity> toEntity (List<PayrollItemRequest> requests,
                                              InstitutionEntity institution){

        return requests.stream().map(

                dto-> {
                    PayrollItemsEntity entity = new PayrollItemsEntity();
                    EmployeeEntity employee = employeeRepository.findByEmployeeIdAndInstitution(dto.employeeId(), institution);

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
                        item.getPayrollItemId(),
                        item.getEmployee().getEmployeeId(),
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
                        item.getMonth(),
                        item.getYear(),
                        item.getPaymentDate(),
                        item.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, payrollItemsEntities.getPageable(), payrollItemsEntities.getTotalElements());
    }

}
