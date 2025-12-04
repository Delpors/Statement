package com.example.statement.payroll;

import com.example.statement.institution.InstitutionEntity;
import com.example.statement.payroll_items.PayrollItemsEntity;
import com.example.statement.period.Period;
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
@Builder
public class PayrollEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollid;

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayrollItemsEntity> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institution_Id")
    private InstitutionEntity institution;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodId")
    private Period period;


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

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "payrollData")
    private LocalDate paymentDate;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        calculateTotals();
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodStatus status = PeriodStatus.DRAFT;

    public enum PeriodStatus{
        DRAFT, CALCULATED, PAID
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
