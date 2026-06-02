package com.example.statement.util;

import com.example.statement.dto.response.TaxesResponse;
import com.example.statement.entity.PayrollEntity;
import com.example.statement.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@RequiredArgsConstructor
public class TaxCalculate {

    private final PayrollRepository payrollRepository;
    private final BigDecimal pfr = BigDecimal.valueOf(0.3);
    private final BigDecimal fss = BigDecimal.valueOf(0.002);

    public Map<Integer, TaxesResponse> getAllFromYearTaxes(Integer year, Long instId){

        List<PayrollEntity> yearPayrollData = payrollRepository
                .findAllByYearAndInstitution_id(year, instId);
        Map<Integer, TaxesResponse> monthlyTaxes;

        monthlyTaxes = Optional.ofNullable(yearPayrollData)
                .orElseGet(Collections::emptyList)
                .stream()
                .collect(Collectors
                        .toMap(
                                PayrollEntity::getMonth,
                                item->{
                                    TaxesResponse taxes = new TaxesResponse();
                                    taxes.setInkomTax(item.getTotalIncomeTax());
                                    taxes.setUnionFee(item.getTotalUnionFee());
                                    BigDecimal totalIncome = Optional.ofNullable(item.getTotalIncome()).orElse(BigDecimal.ZERO);
                                    taxes.setPfrTax(totalIncome.multiply(pfr));
                                    taxes.setFssTax(totalIncome.multiply(fss));
                                    return taxes;
                                }
                        ));

        calculateTotalYearTax(monthlyTaxes);
        return monthlyTaxes;
    }

    private void calculateTotalYearTax(Map<Integer, TaxesResponse> monthlyTaxes){
        TaxesResponse yearlyTotals = new TaxesResponse();

        for (TaxesResponse tax : monthlyTaxes.values()){
            if (tax != null){
                yearlyTotals.setTotalInkomTax(safeAdd(yearlyTotals.getTotalInkomTax(), tax.getInkomTax()));
                yearlyTotals.setTotalUnionFee(safeAdd(yearlyTotals.getTotalUnionFee(), tax.getUnionFee()));
                yearlyTotals.setTotalPfrTax(safeAdd(yearlyTotals.getTotalPfrTax(), tax.getPfrTax()));
                yearlyTotals.setTotalFssTax(safeAdd(yearlyTotals.getTotalFssTax(), tax.getFssTax()));
            }
        }

        BigDecimal grandTotal =
                Stream.of(yearlyTotals.getTotalInkomTax(),
                yearlyTotals.getTotalUnionFee(),
                yearlyTotals.getTotalPfrTax(),
                yearlyTotals.getTotalFssTax())
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        yearlyTotals.setGrandTotal(grandTotal);
        monthlyTaxes.put(0,yearlyTotals);
    }

    private BigDecimal safeAdd(BigDecimal a, BigDecimal b){
        a = a != null? a : BigDecimal.ZERO;
        b = b != null? b : BigDecimal.ZERO;

        return a.add(b);
    }
}
