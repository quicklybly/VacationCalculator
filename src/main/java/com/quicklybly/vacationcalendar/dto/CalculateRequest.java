package com.quicklybly.vacationcalendar.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CalculateRequest(@NotNull @Max(366) @PositiveOrZero Integer daysOfVacation,
                               @NotNull @PositiveOrZero Long averageSalary) {
}
