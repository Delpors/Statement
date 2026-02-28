package com.example.statement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Table(name = "employees")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEntity {

    @Id
    @Column(name = "employee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institution_Id")
    private InstitutionEntity institution;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PayrollItemsEntity> payrolls = new ArrayList<>();


    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surName;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "position")
    private String position;

    @Column(name = "nontaxable")
    private BigDecimal nonTaxable;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "bankaccount")
    private String bankAccount;

    @Column(name = "email")
    private String email;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "deleted_at")
    private LocalDate deletedAt;

    @PrePersist
    protected void onCreate(){
        this.active = true;
    }

    public void softDelete(){
        this.active = false;
        this.deletedAt = LocalDate.now();
    }

    public String getFullName()
    {
        return Stream.of(surName, name, lastname)
                .filter(Objects::nonNull)
                .filter(s->!s.isBlank())
                .collect(Collectors.joining(" "));
    }

    public void restore(){
        this.active = true;
        this.deletedAt = null;
    }

}
