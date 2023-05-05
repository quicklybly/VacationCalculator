package com.quicklybly.vacationcalendar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CalculateResponse(@NotNull @PositiveOrZero Long vacationPay) {
}
