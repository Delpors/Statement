package com.example.statement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "payroll")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayrollEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayrollItemsEntity> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instId")
    private InstitutionEntity institution;

    @Column(name = "totalIncome")
    private BigDecimal totalIncome;

    @Column(name = "totalUnionFee")
    private BigDecimal totalUnionFee;

    @Column(name = "totalIncomeTax")
    private BigDecimal totalIncomeTax;

    @Column(name = "totalAdvance")
    private BigDecimal totalAdvance;

    @Column(name = "totalIssued")
    private BigDecimal totalIssued;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "payrollData")
    private LocalDate paymentDate;

    @PrePersist
    protected void onCreate(){
        System.out.println("Обновление общих сумм при формировании");
        createdAt = LocalDateTime.now();
        calculateTotals();
    }

    @PreUpdate
    protected void onUpdate(){
        System.out.println("Обновление общих сумм при обновлении");
        updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    public void calculateTotals(){
        this.totalIncome = items.stream()
                .map(PayrollItemsEntity::getTotalEmployeeIncome)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalUnionFee = items.stream()
                .map(PayrollItemsEntity::getUnionFee)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalIncomeTax = items.stream()
                .map(PayrollItemsEntity::getIncomeTax)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalAdvance = items.stream()
                .map(PayrollItemsEntity::getAdvance)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalIssued = items.stream()
                .map(PayrollItemsEntity::getTotalIssued)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
