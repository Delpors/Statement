package com.example.statement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "institution")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long institutionId;

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY)
    private List<EmployeeEntity> employee = new ArrayList<>();

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY)
    private List<PayrollItemsEntity> payrollItems = new ArrayList<>();

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY)
    private List<PayrollEntity> payrolls = new ArrayList<>();

    @Column(name = "instit_full_name")
    private String institutionFullName;

    @Column(name = "instit_abbrev")
    private String institutionAbbrev;

    @Column(name = "director")
    private String director;

    @Column(name = "gen_accountant")
    private String generalAccountant;

    @Column(name = "accountant")
    private String accountant;
}
