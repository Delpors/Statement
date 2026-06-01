package com.example.statement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="payroll_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollItemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peurollId")
    private PayrollEntity payroll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instId")
    private InstitutionEntity institution;

    //Начисление

    @Column(name = "base_Salary")//Премии
    private BigDecimal baseSalary;

    @Column(name = "bonus")//Премии
    private BigDecimal bonus;

    @Column(name = "fss")//ФСС 3 дня
    private BigDecimal fss;

    @Column(name = "replace")//Замещение
    private BigDecimal replace;

    @Column(name = "other_income")//Классное руководство
    private BigDecimal otherIncome;

    @Column(name = "total_empl_income")//Общее начисление сотрудника
    private BigDecimal totalEmployeeIncome;


    //Удержание
    @Column(name = "absent") //Прогулы
    private BigDecimal absent;

    @Column(name = "union_fee") //Проф взнос
    private BigDecimal unionFee;

    @Column(name = "income_tax") //Подоходный налог
    private BigDecimal incomeTax;

    @Column(name = "advance") // Аванс
    private BigDecimal advance;

    @Column(name = "total_empl_deduction") // Общее удержание сотрудника
    private BigDecimal totalEmployeeDeduction;

    @Column(name = "amount_issued")//Сумма к выдаче
    private BigDecimal totalIssued;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "payment_data")
    private LocalDate  paymentDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }


}
