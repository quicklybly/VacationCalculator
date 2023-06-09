package com.quicklybly.vacationcalculator.controller;

import com.quicklybly.vacationcalculator.dto.CalculateRequest;
import com.quicklybly.vacationcalculator.dto.CalculateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Calculator API", description = "API for vacation salary calculator")
public interface CalculatorApi {
    @Operation(summary = "Calculate vacation salary")
    @ApiResponse(responseCode = "200",
            description = "Successful operation",
            content = @Content(schema = @Schema(implementation = CalculateResponse.class)))
    @ApiResponse(responseCode = "400",
            description = "Validation error",
            content = @Content(schema = @Schema(implementation = Map.class)))
    CalculateResponse calculate(@RequestBody CalculateRequest request,
                                @Parameter(name = "start",
                                        description = "Date of vacation start",
                                        example = "2023-01-01")
                                Optional<LocalDate> startDate);
}
