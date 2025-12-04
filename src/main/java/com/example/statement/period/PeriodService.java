package com.example.statement.period;

import org.springframework.stereotype.Service;

@Service
public class PeriodService {
    PeriodRepository periodRepository;

    public PeriodService(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    private static String getStatusName(Period.PeriodStatus status){
        return switch (status){
            case DRAFT -> "Черновик";
            case CALCULATED -> "Начислено";
            case PAID -> "Выплачено";
            case CLOSED -> "Завершено";
        };
    }


}
