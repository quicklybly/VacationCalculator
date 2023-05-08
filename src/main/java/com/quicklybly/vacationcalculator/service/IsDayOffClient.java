package com.quicklybly.vacationcalculator.service;

import com.quicklybly.vacationcalculator.enums.DayStatus;

import java.time.LocalDate;
import java.util.Map;

public interface IsDayOffClient {
    Map<LocalDate, DayStatus> getPeriodData(LocalDate startDate, LocalDate endDate);
}
