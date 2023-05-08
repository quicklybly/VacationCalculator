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
        //TODO - check for dummy vacation calendar
        try {
            var days = request.daysOfVacation();
            var averageSalary = request.averageSalary();
            var endDate = startDate.plusDays(days - 1);
            var dayStatusMap = isDayOffClient.getPeriodData(startDate, endDate);
            var workingDays = BigDecimal.valueOf(getNumberOfWorkingDays(dayStatusMap));
            var payment = averageSalary.multiply(workingDays);

            return new CalculateResponse(payment);
        } catch (IntegrationException e) {
            log.error(e.getMessage());
            return calculate(request);
        }
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
}
