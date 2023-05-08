package com.quicklybly.vacationcalculator.service.impl;

import com.quicklybly.vacationcalculator.dto.CalculateRequest;
import com.quicklybly.vacationcalculator.dto.CalculateResponse;
import com.quicklybly.vacationcalculator.enums.DayStatus;
import com.quicklybly.vacationcalculator.exception.IntegrationException;
import com.quicklybly.vacationcalculator.service.CalculatorService;
import com.quicklybly.vacationcalculator.service.IsDayOffClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculatorServiceImpl implements CalculatorService {
    private final IsDayOffClient isDayOffClient;

    @Override
    public CalculateResponse calculate(CalculateRequest request) {
        var days = BigDecimal.valueOf(request.daysOfVacation());
        var averageSalary = request.averageSalary();
        var payment = averageSalary.multiply(days);

        return new CalculateResponse(payment);
    }

    @Override
    public CalculateResponse calculateWithStartDate(CalculateRequest request,
                                                    LocalDate startDate) {
        //TODO think about different code
        var days = request.daysOfVacation();
        var averageSalary = request.averageSalary();
        int workingDays;
        try {
            var endDate = startDate.plusDays(days - 1);
            var dayStatusMap = isDayOffClient.getPeriodData(startDate, endDate);
            workingDays = getNumberOfWorkingDays(dayStatusMap);
        } catch (IntegrationException e) {
            log.error(e.getMessage() + " Using dummy method");
            workingDays = dummyWorkingDaysCounter(startDate, days);
        }
        var payment = averageSalary.multiply(BigDecimal.valueOf(workingDays));

        return new CalculateResponse(payment);
    }

    private int getNumberOfWorkingDays(Map<LocalDate, DayStatus> dayStatusMap) {
        int workingDays = 0;
        for (var status : dayStatusMap.values()) {
            if (DayStatus.WORKING == status) {
                ++workingDays;
            }
        }
        return workingDays;
    }

    private int dummyWorkingDaysCounter(LocalDate startDate, Integer days) {
        int workingDays = 0;
        var currentDate = startDate;
        for (int i = 0; i < days; ++i) {
            var dayOfWeek = currentDate.getDayOfWeek();
            if (DayOfWeek.SATURDAY != dayOfWeek &&
                    DayOfWeek.SUNDAY != dayOfWeek) {
                ++workingDays;
            }
            currentDate = currentDate.plusDays(1);
        }
        return workingDays;
    }
}
