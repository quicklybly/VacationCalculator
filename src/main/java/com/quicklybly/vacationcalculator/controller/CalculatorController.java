package com.quicklybly.vacationcalculator.controller;

import com.quicklybly.vacationcalculator.dto.CalculateRequest;
import com.quicklybly.vacationcalculator.dto.CalculateResponse;
import com.quicklybly.vacationcalculator.service.CalculatorService;
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
        if (startDate.isPresent()) {
            return calculatorService.calculateWithStartDate(request, startDate.get());
        }
        return calculatorService.calculate(request);
    }
}
