package com.quicklybly.vacationcalendar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CalculateResponse(@NotNull @PositiveOrZero BigDecimal vacationPay) {
}
