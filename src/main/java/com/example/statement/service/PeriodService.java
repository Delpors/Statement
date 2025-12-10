package com.example.statement.service;

import com.example.statement.dto.request.PeriodRequest;
import com.example.statement.entity.PeriodEntity;
import com.example.statement.repository.PeriodRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
public class PeriodService {
    PeriodRepository periodRepository;

    public PeriodService(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    public PeriodEntity resolvePeriod(PeriodRequest request) {

        validatePeriodType(request);

        return switch (request.periodType()) {
            case CURRENT_MONTH -> getCurrentMonthPeriod();
            case PREVIOUS_MONTH -> getPreviousPeriod();
            case SPECIFIC_MONTH -> getSpecificMonthPeriod(request.month(), request.year());
            case CUSTOM -> getCustomPeriod(request.startDate(), request.endDate());
            case QUARTER -> getQuarterPeriod(request.quarter());
            case YEAR -> getYearPeriod(request.year());
        };
    }

    private PeriodEntity getPreviousPeriod() {
        LocalDate now = LocalDate.now();
        LocalDate previousMonth = now.minusMonths(1);
        LocalDate startDate = previousMonth.withDayOfMonth(1);
        LocalDate endDate = previousMonth.withDayOfMonth(previousMonth.lengthOfMonth());

        String periodId = String.
                format("month_%d_%02d", previousMonth.getYear(), previousMonth.getMonthValue());

        return periodRepository.findByPeriodId(periodId)
                .orElseGet(() -> createNewPeriod
                        (periodId, startDate, endDate,
                                getMonthName(previousMonth.getMonthValue()) + " " + previousMonth.getYear()));
    }

    private PeriodEntity getSpecificMonthPeriod(Integer month, Integer year) {

        validateMonthAndYear(month, year);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        String periodId = String.format("month_%d_%02d", year, month);

        return periodRepository.findByPeriodId(periodId)
                .orElseGet(() -> createNewPeriod(periodId, startDate, endDate,
                        getMonthName(month) + " " + year));
    }

    private PeriodEntity getCustomPeriod(LocalDate startDate, LocalDate endDate) {

        validateStartEndDate(startDate, endDate);

        String periodId = String.format("custom_%s_%s",
                startDate.format(DateTimeFormatter.BASIC_ISO_DATE),
                endDate.format(DateTimeFormatter.BASIC_ISO_DATE));

        return periodRepository.findByPeriodId(periodId).
                orElseGet(() -> createNewPeriod(periodId, startDate, endDate, String.
                        format("Период с %s по %s",
                                startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                                endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))));
    }

    private PeriodEntity getQuarterPeriod(String quarter) {
        throw new UnsupportedOperationException("Работа с квартальными периодами еще не реализован!");
    }

    private PeriodEntity getYearPeriod(Integer year) {
        throw new UnsupportedOperationException("Работа с годовым периодом еще не реалзован!");
    }

    private PeriodEntity getCurrentMonthPeriod() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        String periodId = String.format("month_%d_%02d", now.getYear(), now.getMonthValue());

        return periodRepository.findByPeriodId(periodId)
                .orElseGet(() -> createNewPeriod
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

    private String getMonthName(int month) {
        return Month.of(month).getDisplayName(TextStyle.FULL,
                new Locale("ru"));
    }

    private void validatePeriodType(PeriodRequest request) {

        if (request.periodType() == null) {

            throw new IllegalArgumentException("Выбор периода обязателен!");
        }

        if (request.periodType() == PeriodRequest.PeriodType.SPECIFIC_MONTH &&
                (request.month() == null || request.year() == null)) {

            throw new IllegalArgumentException("Месяц и год нужно обязательно указать!");
        }

        if (request.periodType() == PeriodRequest.PeriodType.CUSTOM &&
                (request.startDate() == null || request.endDate() == null)) {

            throw new IllegalArgumentException("Начало и конец периода должно быть обязательно указано!");
        }
    }

    private void validateMonthAndYear(Integer month, Integer year){

        if (month < 1 || month > 12){

            throw new IllegalArgumentException("Месяц должен быть между 1 и 12!");
        }
        if (year < 2024 || year > LocalDate.now().getYear()+1){

            throw new IllegalArgumentException("Неизвестное значение для переменной Год!");
        }
    }

    private void validateStartEndDate(LocalDate startDate, LocalDate endDate){
        if (startDate == null || endDate == null) {

            throw new IllegalArgumentException("Для произвольного периода необходимо указать дату начала и окончания периода");
        }

        if (endDate.isBefore(startDate)) {

            throw new IllegalArgumentException("Дата окончания периода не может быть раньше начала периода!");
        }

        if (ChronoUnit.DAYS.between(startDate, endDate) > 366) {

            throw new IllegalArgumentException("В периоде не может быть больше 366 дней!");
        }

    }

}
