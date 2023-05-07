package com.quicklybly.vacationcalendar.service;

import com.quicklybly.vacationcalendar.dto.CalculateRequest;
import com.quicklybly.vacationcalendar.dto.CalculateResponse;

import java.time.LocalDate;

public interface CalculatorService {
    CalculateResponse calculateWithStartDate(CalculateRequest request,
                                             LocalDate startDate);

    CalculateResponse calculate(CalculateRequest request);
}
