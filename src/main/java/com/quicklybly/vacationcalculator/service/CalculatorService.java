package com.quicklybly.vacationcalculator.service;

import com.quicklybly.vacationcalculator.dto.CalculateRequest;
import com.quicklybly.vacationcalculator.dto.CalculateResponse;

import java.time.LocalDate;

public interface CalculatorService {
    CalculateResponse calculateWithStartDate(CalculateRequest request,
                                             LocalDate startDate);

    CalculateResponse calculate(CalculateRequest request);
}
