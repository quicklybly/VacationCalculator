package com.quicklybly.vacationcalendar.service;

import com.quicklybly.vacationcalendar.dto.CalculateRequest;
import com.quicklybly.vacationcalendar.dto.CalculateResponse;

import java.time.LocalDate;
import java.util.Optional;

public interface CalculatorService {
    CalculateResponse calculate(CalculateRequest request,
                                Optional<LocalDate> startDate);
}
