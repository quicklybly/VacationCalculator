package com.quicklybly.vacationcalculator;

import com.quicklybly.vacationcalculator.dto.CalculateRequest;
import com.quicklybly.vacationcalculator.dto.CalculateResponse;
import com.quicklybly.vacationcalculator.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VacationCalculatorApplicationTests {
    @Autowired
    private CalculatorService calculatorService;

    @Test
    void contextLoads() {
    }

    @Test
    void basicCalculationSuccess() {
        var averageSalary = BigDecimal.TEN;
        var days = 10;

        var request = new CalculateRequest(days, averageSalary);
        var response = calculatorService.calculate(request);

        var payment = averageSalary.multiply(BigDecimal.valueOf(days));
        var expected = new CalculateResponse(payment);
        assertThat(response).isEqualTo(expected);
    }


    @Test
    void basicCalculationFail() {
        var averageSalary = BigDecimal.TEN;
        var days = 10;

        var request = new CalculateRequest(days, averageSalary);
        var response = calculatorService.calculate(request);

        var wrongPayment = BigDecimal.ONE;
        var expected = new CalculateResponse(wrongPayment);
        assertThat(response).isNotEqualTo(expected);
    }

    @Test
    void smartCalculationWithWeekendsSuccess() {
        var averageSalary = BigDecimal.TEN;
        var days = 7;
        var startDate = LocalDate.of(2023, 5, 16);

        var request = new CalculateRequest(days, averageSalary);
        var response = calculatorService.calculateWithStartDate(request, startDate);

        var payment = averageSalary.multiply(BigDecimal.valueOf(5));
        var expected = new CalculateResponse(payment);
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void smartCalculationWithWeekendsFail() {
        var averageSalary = BigDecimal.TEN;
        var days = 7;
        var startDate = LocalDate.of(2023, 5, 16);

        var request = new CalculateRequest(days, averageSalary);
        var response = calculatorService.calculateWithStartDate(request, startDate);

        var paymentWrong = averageSalary.multiply(BigDecimal.valueOf(7));
        var expected = new CalculateResponse(paymentWrong);
        assertThat(response).isNotEqualTo(expected);
    }
}
