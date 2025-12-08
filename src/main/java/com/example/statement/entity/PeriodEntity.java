package com.example.statement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PeriodEntity {

    @Id
    private String periodId;

    @OneToOne(mappedBy = "period")
    private PayrollEntity payroll = new PayrollEntity();

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodStatus status= PeriodStatus.DRAFT;

    public enum PeriodStatus{
        DRAFT,
        CALCULATED,
        PAID,
        CLOSED
    }

    public enum PeriodType{
        CURRENT_MONTH,
        PREVIOUS_MONTH,
        SPECIFIC_MONTH,
        CUSTOM,
        QUARTER,
        YEAR
    }

}
