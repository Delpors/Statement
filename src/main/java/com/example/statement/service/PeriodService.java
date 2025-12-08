package com.example.statement.service;

import com.example.statement.entity.PeriodEntity;
import com.example.statement.repository.PeriodRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
public class PeriodService {
    PeriodRepository periodRepository;

    public PeriodService(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }



    private PeriodEntity getCurrentMonthPeriod(){
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        String periodId = String.format("month_%d_%02d", now.getYear(), now.getMonthValue());

        return periodRepository.findByPeriodId(periodId)
                .orElseGet(()->createNewPeriod
                        (periodId, startDate, endDate,
                                getMonthName(now.getMonthValue()) + " " + now.getYear()));
    }

    private PeriodEntity createNewPeriod(String periodId, LocalDate startDate, LocalDate endDate, String monthName) {
        PeriodEntity period = new PeriodEntity();

        period.setPeriodId(periodId);
        period.setStartDate(startDate);
        period.setEndDate(endDate);
        period.setName(monthName);

        return periodRepository.save(period);
    }

    private String getMonthName(int month){
        return Month.of(month).getDisplayName(TextStyle.FULL, new Locale("ru"));
    }

    public String getStatusName(PeriodEntity.PeriodStatus status){
        return switch (status){
            case DRAFT -> "Черновик";
            case CALCULATED -> "Начислено";
            case PAID -> "Выплачено";
            case CLOSED -> "Завершено";
        };
    }

    public String getPeriodType(PeriodEntity.PeriodType type){
        return switch (type){
            case CURRENT_MONTH -> "Тукущий месяц";
            case PREVIOUS_MONTH -> "Предыдущий месяц";
            case SPECIFIC_MONTH -> "Месяц";
            case CUSTOM -> "Произвольный период";
            case QUARTER -> "Квартал";
            case YEAR -> "Год";
        };
    }

}
