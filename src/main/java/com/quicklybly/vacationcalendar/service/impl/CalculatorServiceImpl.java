package com.quicklybly.vacationcalendar.service.impl;

import com.quicklybly.vacationcalendar.dto.CalculateRequest;
import com.quicklybly.vacationcalendar.dto.CalculateResponse;
import com.quicklybly.vacationcalendar.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class CalculatorServiceImpl implements CalculatorService {
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
        return null;
    }
}
