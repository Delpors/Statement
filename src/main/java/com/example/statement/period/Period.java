package com.example.statement.period;

import com.example.statement.payroll.PayrollEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long periodId;

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
        DRAFT, CALCULATED, PAID, CLOSED
    }

}
