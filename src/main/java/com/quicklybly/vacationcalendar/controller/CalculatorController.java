package com.quicklybly.vacationcalendar.controller;

import com.quicklybly.vacationcalendar.dto.CalculateRequest;
import com.quicklybly.vacationcalendar.dto.CalculateResponse;
import com.quicklybly.vacationcalendar.service.CalculatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class CalculatorController implements CalculatorApi {
    private final CalculatorService calculatorService;

    @Override
    @PostMapping("/calculate")
    public CalculateResponse calculate(@RequestBody @Valid CalculateRequest request,
                                       @RequestParam(name = "start", required = false) Optional<LocalDate> startDate) {
        return calculatorService.calculate(request, startDate);
    }
}
