package com.example.statement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxesResponse {
    private BigDecimal inkomTax;
    private BigDecimal unionFee;
    private BigDecimal pfrTax;
    private BigDecimal fssTax;
    private BigDecimal totalInkomTax;
    private BigDecimal totalUnionFee;
    private BigDecimal totalPfrTax;
    private BigDecimal totalFssTax;
    private BigDecimal grandTotal;

}
